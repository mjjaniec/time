package com.github.mjjaniec.time.runner;

import com.github.mjjaniec.time.loader.Loggers;
import com.github.mjjaniec.time.loader.Runner;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String... args) {
        Logger logger = Loggers.get(Main.class);

        try {
            Path pending = Paths.get("updater.jar.pending");
            Path current = Paths.get("updater.jar");

            if (pending.toFile().exists()) {
                logger.info("Replaced updater.jar with updater.jar.pending and delete pending file...");
                Files.copy(pending, current, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(pending);
                logger.info("done!");
            } else {
                logger.info("No pending updater.");
            }
            logger.info("Starting updater.jar...");
            Runner.loadJar(current);
            logger.info("Updater started. Exiting.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
}
