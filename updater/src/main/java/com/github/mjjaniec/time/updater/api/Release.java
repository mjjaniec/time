package com.github.mjjaniec.time.updater.api;

import com.github.mjjaniec.time.updater.Version;

import java.util.List;

public class Release {
    private String tag_name;
    private List<Asset> assets;

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public Version version() {
        return new Version(tag_name);
    }
}
