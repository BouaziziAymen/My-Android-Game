package com.evolgames.entities;

public class ItemCategory {
    String filePath;
    String categoryName;

    public ItemCategory(String filePath, String categoryName) {
        this.filePath = filePath;
        this.categoryName = categoryName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
