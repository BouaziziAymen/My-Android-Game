package com.evolgames.helpers;

import com.evolgames.userinterface.model.ItemCategory;

public class ItemMetaData {
    private String fileName;
    private String toolName;
    private ItemCategory itemCategory;
    private boolean userCreated;

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

    public void setUserCreated(boolean userCreated) {
        this.userCreated = userCreated;
    }

    public boolean isUserCreated() {
        return userCreated;
    }
}
