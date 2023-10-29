package com.evolgames.userinterface.view.inputs;


import android.graphics.Color;

import com.evolgames.gameengine.ResourceManager;
import com.evolgames.userinterface.control.behaviors.ButtonBehavior;
import com.evolgames.userinterface.control.behaviors.ColorSelectorBehavior;
import com.evolgames.userinterface.control.windowcontrollers.LinearLayoutAdvancedWindowController;
import com.evolgames.userinterface.control.windowcontrollers.gamewindowcontrollers.ColorSelectorWindowController;

import org.andengine.entity.primitive.DrawMode;
import org.andengine.entity.primitive.Mesh;


public class ColorSelector extends ClickableImage<ColorSelectorWindowController, ColorSelectorBehavior> {
    private final Button<ColorSelectorWindowController> removeColorButton;
    private final Mesh mesh;
    private float value;
    private float saturation;
    private float hue;
    private float red;
    private float green;
    private float blue;
    private float alpha;
    public ColorSelector(float pX, float pY, ColorSelectorWindowController controller) {
        super(pX, pY, ResourceManager.getInstance().colorSelectorTextureRegion, false);


        float[] circle_vertices = new float[24 * 3];
        float RAY = 8f;


        for (int i = 0; i < 24; i++) {
            float theta = (float) (2 * Math.PI * (float) i / (float) 24);//get the current angle

            circle_vertices[3 * i] = (float) (RAY * Math.cos(theta));//calculate the x component
            circle_vertices[3 * i + 1] = (float) (RAY * Math.sin(theta));//calculate the y component
            circle_vertices[3 * i + 2] = 0;
        }
        this.mesh = new Mesh(0, 0, circle_vertices, 24, DrawMode.TRIANGLE_FAN, ResourceManager.getInstance().vbom);


        this.value = 100;
        this.alpha = 1f;
        setBehavior(new ColorSelectorBehavior(controller, this));


        Button<LinearLayoutAdvancedWindowController<?>> addColorButton = new Button<>(ResourceManager.getInstance().add1, Button.ButtonType.OneClick, true);
        addColorButton.setPosition(17 - addColorButton.getWidth() / 2, 33 - addColorButton.getHeight() / 2);
        addColorButton.setBehavior(new ButtonBehavior<LinearLayoutAdvancedWindowController<?>>(controller, addColorButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.addColorToPanel();
            }

            @Override
            public void informControllerButtonReleased() {

            }
        });
        addElement(addColorButton);


        removeColorButton = new Button<>(ResourceManager.getInstance().minus1, Button.ButtonType.OneClick, true);
        removeColorButton.setPosition(33 - removeColorButton.getWidth() / 2, 17 - removeColorButton.getHeight() / 2);
        removeColorButton.setBehavior(new ButtonBehavior<ColorSelectorWindowController>(controller, removeColorButton) {
            @Override
            public void informControllerButtonClicked() {
                controller.removeColorFromPanel();
            }

            @Override
            public void informControllerButtonReleased() {

            }
        });
        addElement(removeColorButton);
        removeColorButton.updateState(Button.State.DISABLED);

    }

    public Button<ColorSelectorWindowController> getRemoveColorButton() {
        return removeColorButton;
    }

    @Override
    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    @Override
    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    @Override
    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public void updateColorHSV() {

        int color = Color.HSVToColor(new float[]{this.hue, this.saturation, this.value});
        red = Color.red(color) / (float) 255;
        green = Color.green(color) / (float) 255;
        blue = Color.blue(color) / (float) 255;
        updateMeshColor();
    }

    public void updateMeshColor() {
        mesh.setColor(red, green, blue);
        mesh.setAlpha(alpha);
    }

    public void updateColorRGB() {
        float[] hsv = new float[3];
        Color.RGBToHSV((int) Math.floor(red * 255), (int) Math.floor(green * 255), (int) Math.floor(blue * 255), hsv);
        hue = hsv[0];
        saturation = hsv[1];
        value = hsv[2];
        updateMeshColor();

    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
