package com.evolgames.userinterface.view;
public class ShiftText {
    private String text;

    public ShiftText(String text) {
        this.text = text;
    }

    public void update() {
        text = text.substring(1) + text.charAt(0);
        System.out.println(text);
    }
}
