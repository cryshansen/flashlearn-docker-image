package com.artog.flashlearn.dto;


public class UpgradeRequest {
    private String newTier;  // FREE, MIDDLE, TOP

    public String getNewTier() { return newTier; }
    public void setNewTier(String newTier) { this.newTier = newTier; }
}