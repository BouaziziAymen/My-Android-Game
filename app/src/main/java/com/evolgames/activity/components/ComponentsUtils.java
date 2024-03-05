package com.evolgames.activity.components;

import android.app.Activity;
import android.content.res.Resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentsUtils {
    public static int dpToPixels(float dpValue, Resources resources) {
        float density = resources.getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    public static List<String> list(String... strings) {
        return Arrays.stream(Arrays.stream(strings).toArray(String[]::new)).collect(Collectors.toList());
    }
}
