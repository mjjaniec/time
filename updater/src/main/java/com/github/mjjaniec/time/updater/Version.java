package com.github.mjjaniec.time.updater;

public class Version {
    public final static Version Null = new Version("0.0.0");

    @SuppressWarnings("WeakerAccess")
    public final int major, minor, build;

    public Version(String version) {
        String[] parts = version.split("[.]");
        major = Integer.parseInt(parts[0]);
        minor = Integer.parseInt(parts[1]);
        build = Integer.parseInt(parts.length == 3 ? parts[2] : "0");
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + build;
    }

    public boolean isNewer(Version other) {
        if (major != other.major) {
            return major > other.major;
        }
        if (minor != other.minor) {
            return minor > other.minor;
        }
        return build > other.build;
    }
}
