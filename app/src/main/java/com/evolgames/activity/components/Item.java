package com.evolgames.activity.components;

import androidx.annotation.NonNull;

public class Item {
    private String titleTranslated;
    private String title;

    public Item(String titleTranslated, String title) {
        this.titleTranslated = titleTranslated;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleTranslated() {
        return titleTranslated;
    }

    @NonNull
    @Override
    public String toString() {
        return titleTranslated;
    }
}
