package com.evolgames.entities;

public class ItemCategory {
    String prefix;
    String categoryName;

    public ItemCategory(String filePath, String categoryName) {
        this.prefix = filePath;
        this.categoryName = categoryName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
