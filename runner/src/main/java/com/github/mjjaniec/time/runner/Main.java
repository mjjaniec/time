package com.github.mjjaniec.time.runner;

import com.github.mjjaniec.time.commons.Loggers;
import com.github.mjjaniec.time.commons.Runner;

import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String... args) {
        Logger logger = Loggers.get(Main.class);
        logger.info("Debug info:\n" +
                "  java.version: " + System.getProperty("java.version") + "\n" +
                "  java.home: " + System.getProperty("java.home") + "\n" +
                "  os.name: " + System.getProperty("os.name") + "\n" +
                "  file.encoding: " + System.getProperty("file.encoding") + "\n" +
                "  user.dir: " + System.getProperty("user.dir"));

        try {
            Path pending = Paths.get("updater.jar.pending");
            Path current = Paths.get("updater.jar");

            if (pending.toFile().exists()) {
                logger.info("Replace updater.jar with updater.jar.pending and delete .pending file...");
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
