package com.github.mjjaniec.time.runner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String... args) throws IOException {
        FileHandler fh = new FileHandler("runner.log");
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        Logger logger = Logger.getLogger("Runner");
        logger.addHandler(fh);

        try {
            Path pending = Paths.get("updater.jar.pending");
            Path current = Paths.get("updater.jar");

            if (pending.toFile().exists()) {
                Files.copy(pending, current, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(pending);
            }

            URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(sysLoader, current.toUri().toURL());

            Class<?> main = sysLoader.loadClass("com.github.mjjaniec.time.updater.Main");
            String[] mainParameters = new String[0];
            main.getMethod("main", String[].class).invoke(null, (Object) mainParameters);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
}
