package com.artog.flashlearn.dto;

public class ProviderInfo {
    private String name;
    private boolean active;
    private String description;

    public ProviderInfo(String name, boolean active, String description) {
        this.name = name;
        this.active = active;
        this.description = description;
    }

    // getters
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public String getDescription() { return description; }
}

