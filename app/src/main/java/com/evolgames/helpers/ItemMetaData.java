package com.evolgames.helpers;

import com.evolgames.userinterface.model.ItemCategory;

public class ItemMetaData {
    private String fileName;
    private String toolName;
    private ItemCategory itemCategory;

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
}
