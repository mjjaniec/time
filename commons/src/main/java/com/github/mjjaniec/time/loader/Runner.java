package com.github.mjjaniec.time.loader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Runner {

    public static void loadJar(Path jarPath, String... arguments) throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaPath = Paths.get(javaHome, "bin", "java").toString();
        String[] command = new String[arguments.length + 3];
        command[0] = javaPath;
        command[1] = "-jar";
        command[2] = jarPath.toAbsolutePath().toString();
        System.arraycopy(arguments, 0, command, 3, arguments.length);

        new ProcessBuilder().command(command).inheritIO().start();
    }

}
