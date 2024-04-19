package com.evolgames.helpers;

import com.evolgames.userinterface.model.ItemCategory;

public class ItemMetaData {
    private String fileName;
    private String toolName;
    private ItemCategory itemCategory;
    private boolean userCreated;
    private String templateName;
    private String displayName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public boolean isUserCreated() {
        return userCreated;
    }

    public void setUserCreated(boolean userCreated) {
        this.userCreated = userCreated;
    }

    public void setTemplateFilename(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
