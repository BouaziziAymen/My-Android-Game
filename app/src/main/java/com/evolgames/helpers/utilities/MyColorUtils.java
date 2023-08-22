package com.evolgames.helpers.utilities;

import android.graphics.Bitmap;

import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

public class MyColorUtils {
    public static final Color bloodColor = new Color(175 / 256f, 17 / 256f, 28 / 256f);
    private static final ArrayList<Color> gradient;
    private static final float[] limits = new float[]{0,500, 1000, 6000, 12000, 100000};

    private static final ColorTest testBurnRatioAction = new ColorTest() {
        @Override
        public void testFactor(Color color, double burnRatio, ColorAction action) {
            if (burnRatio > 0.01f) {
                action.performAction(color, (float) burnRatio);
            }
        }
    };
    private static ColorAction setTextureColorAction;
    private static ColorAction testTextureColorAction = new ColorAction() {
        @Override
        public void performAction(Color color, float burnRatio) {
            result = true;
        }
    };
    private static ColorAction setRadianceColorAction = new ColorAction() {
        @Override
        public void performAction(Color color, float temperature) {
            result = true;
            float alpha;
            color.set(MyColorUtils.getColor(temperature));
            if (temperature > 1000f) alpha = 1f;
            else if(temperature>300f)alpha = (temperature-300f) / 700f;
            else alpha = 0;
            color.setAlpha(alpha*0.5f);
        }
    };
    private static ColorAction testRadianceAction = new ColorAction() {
        @Override
        public void performAction(Color color, float temperature) {
            result = true;
        }
    };
    private static ColorTest testTemperatureAction = new ColorTest() {

        @Override
        public void testFactor(Color color, double temperature, ColorAction action) {
                action.performAction(color, (float) temperature);
        }
    };


    static {
        setTextureColorAction = new ColorAction() {
            @Override
            public void performAction(Color color, float burnRatio) {
                result = true;
                color.set(0.1f, 0.1f, 0.1f, burnRatio);
            }
        };
        gradient = new ArrayList<>();
        gradient.add(new Color(0.5f, 0, 0));
        gradient.add(new Color(1, 0, 0));
        gradient.add(new Color(1, 100 / 255f, 0));
        gradient.add(new Color(1, 1, 1));
        gradient.add(new Color(0.5f, 0.5f, 1f));
        gradient.add(new Color(0, 0, 1f));


        ArrayList<Color> flameColors = new ArrayList<>();
        flameColors.add(Color.RED);//600K/800K
        flameColors.add(new Color(1f, 215 / 255f, 0));
        flameColors.add(Color.YELLOW);
        flameColors.add(Color.CYAN);
        flameColors.add(Color.BLUE);
        flameColors.add(Color.WHITE);

    }
public static float getPreviousTemperature(double temperature){
    int i;
    for (i = 1; i < 4; i++) {
        if (temperature < limits[i]) {
            break;
        }
    }
    return limits[i-1];
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
        return new Color(color1.getRed() + ratio * deltaRed, color1.getGreen() + ratio * deltaGreen, color1.getBlue() + ratio * deltaBlue);
    }

    public static void setupFlameColor(Color defaultColor,double temperature) {
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
        defaultColor.set(color1.getRed() + ratio * deltaRed, color1.getGreen() + ratio * deltaGreen, color1.getBlue() + ratio * deltaBlue);
    }

    public static void blendColors(Color resultColor, Color background, Color foreground) {

        float a01 = (1 - foreground.getAlpha())*background.getAlpha() + foreground.getAlpha();

        float a0 = foreground.getAlpha();
        float a1 = background.getAlpha();
        float r0 = foreground.getRed();
        float r1 = background.getRed();

        float g0 = foreground.getGreen();
        float g1 = background.getGreen();

        float b0 = foreground.getBlue();
        float b1 = background.getBlue();

        float r01 = ((1 - a0)*a1*r1 + a0*r0);

        float g01 = ((1 - a0)*a1*g1 + a0*g0);

        float b01 = ((1 - a0)*a1*b1 + a0*b0);
       resultColor.set(r01,g01,b01,a01);
    }

    public static int getTemperatureLevel(float temperature) {

        if (temperature < 600) return 0;
        else if (temperature < 1000) return 1;
        else if (temperature < 2000) return 2;
        else if (temperature < 3000) return 3;
        else if (temperature < 4000) return 4;
        else if (temperature < 5000) return 5;
        else if (temperature < 6000) return 6;
        else if (temperature < 7000) return 7;
        else if (temperature < 8000) return 8;
        else if (temperature < 9000) return 9;
        else if (temperature < 10000) return 10;
        else return 11;

    }

    public static boolean setRadianceColor(Color color, double temperature) {
        setRadianceColorAction.result = false;
        testTemperatureAction.testFactor(color, temperature, setRadianceColorAction);
        return setRadianceColorAction.result;
    }

    public static boolean setTextureColor(Color color, double burnRatio) {
        setTextureColorAction.result = false;
        testBurnRatioAction.testFactor(color, burnRatio, setTextureColorAction);
        return setTextureColorAction.result;
    }

    public static boolean hasCoatingColor(float temperature, float burnRatio) {
        testRadianceAction.result = false;
        testTextureColorAction.result = false;
        testTemperatureAction.testFactor(null, temperature, testRadianceAction);
        testBurnRatioAction.testFactor(null, burnRatio, testTextureColorAction);
        return testTextureColorAction.result && testRadianceAction.result;
    }

    abstract static class ColorTest {
        public abstract void testFactor(Color color, double factor, ColorAction action);

    }

    abstract static class ColorAction {
        boolean result;

        public abstract void performAction(Color color, float factor);

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
                if (i < 0 || i >= width || j < 0 || j >= height)
                    continue;

                /* If the pixel is outside the circle, skip it */
                if (MathUtils.distance(x, y, i, j) > r)
                    continue;


                /* Get the color from the image, add to a running sum */
                int c = img.getPixel(i, j);
                r += red(c);
                g += green(c);
                b += blue(c);
                num++;
            }
        }
        /* Return the mean of the R, G, and B components */
        return new Color(r/num/255f, g/num/255f, b/num/255f);
    }
}
