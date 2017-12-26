package com.github.mjjaniec.time.commons;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.logging.*;

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
        FileHandler fh = new FileHandler("" + getModuleName(clazz) + ".log");
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                String lvl = record.getLevel().getName();
                String throwable = "";
                if (record.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    throwable = sw.toString();
                }
                return Instant.ofEpochMilli(record.getMillis()).toString().replaceAll("[A-Z]", " ") +
                        lvl + "       ".substring(lvl.length()) +
                        "[" + record.getLoggerName() + "][" + record.getSourceMethodName() + "] " +
                        record.getMessage() + throwable + "\n";
            }
        };

        Logger logger = Logger.getLogger("");
        logger.addHandler(fh);
        for (Handler h : logger.getHandlers()) {
            h.setFormatter(formatter);
        }
    }

    private static String getModuleName(Class<?> clazz) {
        return clazz.getName()
                .replaceAll("com[.]github[.]mjjaniec[.]time[.]", "")
                .replaceAll("[.].*", "");
    }
}
