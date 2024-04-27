package com.evolgames.dollmutilationgame.utilities;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

import android.graphics.Bitmap;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

public class MyColorUtils {
    public static final Color bloodColor = new Color(175 / 256f, 17 / 256f, 28 / 256f,0.8f);
    private static final ArrayList<Color> gradient;
    private static final float[] limits = new float[]{0, 500, 1000, 6000, 12000, 100000};
  public static Color frostColor =  new Color(172f/255f, 213f/255f, 243f/255f);

    private static final ColorAction setRadianceColorAction =
            new ColorAction() {
                @Override
                public void performAction(Color color, float temperature) {
                    result = true;
                    float alpha;
                    color.set(MyColorUtils.getColor(temperature));
                    if (temperature > 1000f) {
                        alpha = 0.5f;
                    } else if (temperature > 300f) {
                        alpha = 0.5f * (temperature - 300f) / 700f;
                    } else {
                        alpha = 0;
                    }
                    color.setAlpha(alpha * 0.25f);
                }
            };
    private static final ColorTest testTemperatureAction =
            new ColorTest() {

                @Override
                public void testFactor(Color color, double temperature, ColorAction action) {
                    action.performAction(color, (float) temperature);
                }
            };

    static {
        gradient = new ArrayList<>();
        gradient.add(new Color(0.5f, 0, 0));
        gradient.add(new Color(1f, 0, 0));
        gradient.add(new Color(1, 100 / 255f, 0));
        gradient.add(new Color(1, 1, 1));
        gradient.add(new Color(0.5f, 0.5f, 1f));
        gradient.add(new Color(0, 0, 1f));
    }

    public static float getPreviousTemperature(double temperature) {
        int i;
        for (i = 1; i < 4; i++) {
            if (temperature < limits[i]) {
                break;
            }
        }
        return limits[i - 1];
    }

    public static Color getColor(double temperature) {
        int i;
        for (i = 1; i < 4; i++) {
            if (temperature < limits[i]) {
                break;
            }
        }
        Color color1 = gradient.get(i - 1);
        Color color2 = gradient.get(i);
        float ratio = (float) ((temperature - limits[i - 1]) / (limits[i] - limits[i - 1]));
        float deltaRed = color2.getRed() - color1.getRed();
        float deltaGreen = color2.getGreen() - color1.getGreen();
        float deltaBlue = color2.getBlue() - color1.getBlue();
        return new Color(
                color1.getRed() + ratio * deltaRed,
                color1.getGreen() + ratio * deltaGreen,
                color1.getBlue() + ratio * deltaBlue);
    }

    public static void setupFlameColor(Color defaultColor, double temperature) {
        int i;
        for (i = 1; i < 4; i++) {
            if (temperature < limits[i]) break;
        }
        Color color1 = gradient.get(i - 1);
        Color color2 = gradient.get(i);
        float ratio = (float) ((temperature - limits[i - 1]) / (limits[i] - limits[i - 1]));
        float deltaRed = color2.getRed() - color1.getRed();
        float deltaGreen = color2.getGreen() - color1.getGreen();
        float deltaBlue = color2.getBlue() - color1.getBlue();
        defaultColor.set(
                color1.getRed() + ratio * deltaRed,
                color1.getGreen() + ratio * deltaGreen,
                color1.getBlue() + ratio * deltaBlue);
    }

    public static void blendColors(Color resultColor, Color background, Color foreground) {

        float a01 = (1 - foreground.getAlpha()) * background.getAlpha() + foreground.getAlpha();

        float a0 = foreground.getAlpha();
        float a1 = background.getAlpha();
        float r0 = foreground.getRed();
        float r1 = background.getRed();

        float g0 = foreground.getGreen();
        float g1 = background.getGreen();

        float b0 = foreground.getBlue();
        float b1 = background.getBlue();

        float r01 = ((1 - a0) * a1 * r1 + a0 * r0);

        float g01 = ((1 - a0) * a1 * g1 + a0 * g0);

        float b01 = ((1 - a0) * a1 * b1 + a0 * b0);
        resultColor.set(r01, g01, b01, a01);
    }

    public static boolean setRadianceColor(Color color, double temperature) {
        setRadianceColorAction.result = false;
        testTemperatureAction.testFactor(color, temperature, setRadianceColorAction);
        return setRadianceColorAction.result;
    }

    public static boolean setTextureColor(Color color, double burnRatio, double frostRatio) {
        color.set(0,0,0,0f);
        boolean hasTexture = false;
        if(burnRatio > 0.01f) {
            hasTexture = true;
            color.set(0.1f, 0.1f, 0.1f, (float) (burnRatio / 2f));
        }

      frostColor.setAlpha((float) frostRatio);
        if(frostRatio>0.7){
            hasTexture = true;
            MyColorUtils.blendColors(color, color,frostColor);
        }

        return hasTexture;
    }

    public static Color getAverageRGBCircle(Bitmap img, int x, int y, int radius) {
        float r = 0;
        float g = 0;
        float b = 0;
        int num = 0;
        int width = img.getWidth();
        int height = img.getHeight();
        /* Iterate through a bounding box in which the circle lies */
        for (int i = x - radius; i < x + radius; i++) {
            for (int j = y - radius; j < y + radius; j++) {
                /* If the pixel is outside the canvas, skip it */
                if (i < 0 || i >= width || j < 0 || j >= height) continue;

                /* If the pixel is outside the circle, skip it */
                if (MathUtils.distance(x, y, i, j) > r) continue;

                /* Get the color from the image, add to a running sum */
                int c = img.getPixel(i, j);
                r += red(c);
                g += green(c);
                b += blue(c);
                num++;
            }
        }
        /* Return the mean of the R, G, and B components */
        return new Color(r / num / 255f, g / num / 255f, b / num / 255f);
    }

    abstract static class ColorTest {
        public abstract void testFactor(Color color, double factor, ColorAction action);
    }

    abstract static class ColorAction {
        boolean result;

        public abstract void performAction(Color color, float factor);
    }
}
