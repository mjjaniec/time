package com.github.mjjaniec.time.commons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Runner {

    public static void loadJar(Path jarPath, String... arguments) throws IOException {
        File jarFile = jarPath.toFile();
        if (!jarFile.exists()) {
            throw new RuntimeException("File '" + jarPath.toAbsolutePath().toString() + "' does not exists.");
        }
        if (!jarFile.canRead()) {
            throw new RuntimeException("File '" + jarPath.toAbsolutePath().toString() + "' cannot be read.");
        }
        String javaHome = System.getProperty("java.home");
        String javaPath = Paths.get(javaHome, "bin", "java").toString();
        String[] command = new String[arguments.length + 4];
        command[0] = javaPath;
        command[1] = "-Dfile.encoding=UTF-8";
        command[2] = "-jar";
        command[3] = jarPath.toAbsolutePath().toString();
        System.arraycopy(arguments, 0, command, 4, arguments.length);

        new ProcessBuilder().command(command).inheritIO().start();
    }

}
