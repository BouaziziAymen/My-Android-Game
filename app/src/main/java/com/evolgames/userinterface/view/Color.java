package com.evolgames.userinterface.view;

public class Color {
    private float red;
    private float green;
    private float blue;
    private float opacity;

    public Color(float red, float green, float blue, float opacity) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }

    public Color(float red, float green, float blue) {
        this(red, green, blue, 1);
    }

    public float getRed() {
        return red/255f;
    }

    public float getGreen() {
        return green/255f;
    }

    public float getBlue() {
        return blue/255f;
    }

    public float getOpacity() {
        return opacity;
    }
}
