package com.github.mjjaniec.time.updater;

import com.github.mjjaniec.time.loader.Loggers;
import com.github.mjjaniec.time.loader.Runner;
import com.github.mjjaniec.time.updater.api.Asset;
import com.github.mjjaniec.time.updater.api.Release;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final String CoreJar = "core.jar";
    private static final String UpdaterJar = "updater.jar";

    private static Logger LOGGER = Loggers.get(Main.class);

    public static void main(String... args) {

        LocalVersionFacade local = new LocalVersionFacade();
        GithubApiFacade github = new GithubApiFacade();

        Optional<Version> currentRelease = local.currentRelease();
        Optional<Release> latestRelease = github.latest();

        Optional<Release> updated = latestRelease.
                filter(latest -> latest.version().isNewer(currentRelease.orElse(Version.Null)))
                .flatMap(latest -> update(latest, local));


        startApplication(updated);
    }

    private static Optional<Release> update(Release release, LocalVersionFacade local) {
        try {
            List<Asset> assets = release.getAssets();
            Optional<Asset> updaterAsset = assets.stream().filter(a -> UpdaterJar.equals(a.getName())).findFirst();
            if (!updaterAsset.isPresent()) {
                LOGGER.warning("No updater asset in the release " + release.version().toString() + ".");
            }
            updaterAsset.ifPresent(asset -> downloadAsset(asset, asset.getName() + ".pending"));

            Optional<Asset> coreAsset = assets.stream().filter(a -> CoreJar.equals(a.getName())).findFirst();
            if (!coreAsset.isPresent()) {
                LOGGER.warning("No core asset in the release " + release.version().toString() + ". Update failed!");
            }
            return coreAsset.map(asset -> {
                downloadAsset(asset, asset.getName());
                local.saveCurrentRelease(release.version());
                return release;
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Update failed", e);
            return Optional.empty();
        }
    }

    private static void downloadAsset(Asset asset, String targetFile) {
        try {
            URL url = new URL(asset.getBrowser_download_url());
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(targetFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void startApplication(Optional<Release> updated) {
        try {
            String[] arguments = updated.map(release -> new String[]{
                    release.version().toString(),
                    release.getBody()
            }).orElse(new String[0]);
            Runner.loadJar(Paths.get(CoreJar), arguments);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Start failed", e);
        }
    }
}
