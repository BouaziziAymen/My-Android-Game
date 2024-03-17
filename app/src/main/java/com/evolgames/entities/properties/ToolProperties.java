package com.evolgames.entities.properties;

public class ToolProperties extends Properties {
    private String toolName;
    private boolean published;

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
