package com.github.mjjaniec.time.loader;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Loggers {

    private static boolean initialized = false;


    public static Logger get(Class<?> clazz) {
        try {
            initializeIfNeeded(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Logger.getLogger(clazz.getName());
    }

    private static void initializeIfNeeded(Class<?> clazz) throws IOException {
        if (initialized) {
            return;
        }
        synchronized (Loggers.class) {
            if (initialized) {
                return;
            }
            initialize(clazz);
            initialized = true;
        }
    }

    private static void initialize(Class<?> clazz) throws IOException {
        FileHandler fh = new FileHandler(getModuleName(clazz) + ".log");
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        Logger logger = Logger.getLogger("");
        logger.addHandler(fh);
    }

    private static String getModuleName(Class<?> clazz) {
        return clazz.getName()
                .replaceAll("com[.]github[.]mjjaniec[.]time[.]", "")
                .replaceAll("[.].*", "");
    }
}
