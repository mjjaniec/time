package com.github.mjjaniec.time.updater;

import com.github.mjjaniec.time.loader.Loggers;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalVersionFacade {
    private static final Logger LOGGER = Loggers.get(LocalVersionFacade.class);
    private static final String CurrentRelease = "release.ver";

    public Optional<Version> currentRelease() {
        try (Scanner scanner = new Scanner(new FileReader(CurrentRelease))) {
            return Optional.ofNullable(scanner.next()).map(Version::new);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Cannot read current version", e);
            return Optional.empty();
        }
    }

    public void saveCurrentRelease(Version version) {
        try (FileWriter writer = new FileWriter(CurrentRelease)) {
            writer.write(version.toString());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Cannot save current version", e);
        }
    }
}
