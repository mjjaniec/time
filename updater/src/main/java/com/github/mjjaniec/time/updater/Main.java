package com.github.mjjaniec.time.updater;

import com.github.mjjaniec.time.updater.api.Release;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;

public class Main {
    private static final String CoreJar = "core.jar";

    public static void main(String... args) {

        LocalVersionFacade local = new LocalVersionFacade();
        GithubApiFacade github = new GithubApiFacade();

        Optional<Version> currentRelease = local.currentRelease();
        Optional<Release> latestRelease = github.latest();

        latestRelease.
                filter(latest -> latest.version().isNewer(currentRelease.orElse(Version.Null)))
                .ifPresent(latest -> update(latest, local));


        startApplication();
    }

    private static void update(Release release, LocalVersionFacade local) {
        try {
            URL url = new URL(release.getAssets().get(0).getBrowser_download_url());
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(CoreJar);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            local.saveCurrentRelease(release.version());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startApplication() {
        try {
            File myJar = new File(CoreJar);

            URL url = myJar.toURI().toURL();

            Class[] parameters = new Class[]{URL.class};

            URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class<URLClassLoader> sysClass = URLClassLoader.class;

            Method method = sysClass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysLoader, url);

            Class<?> main = ClassLoader.getSystemClassLoader().loadClass("com.github.mjjaniec.time.core.Main");
            String[] arguments = new String[0];
            main.getMethod("main", String[].class).invoke(null, (Object) arguments);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
