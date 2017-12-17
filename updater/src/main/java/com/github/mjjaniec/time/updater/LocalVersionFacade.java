package com.github.mjjaniec.time.updater;

import com.github.mjjaniec.time.updater.api.Release;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class LocalVersionFacade {
    private static final String CurrentRelease = "CurrentRelease.v";
    public Optional<Version> currentRelease() {
        try(Scanner scanner = new Scanner(new FileReader(CurrentRelease))) {
            return Optional.ofNullable(scanner.next()).map(Version::new);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void saveCurrentRelease(Version version) {
        try(FileWriter writer = new FileWriter(CurrentRelease)) {
            writer.write(version.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
