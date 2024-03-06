package com.evolgames.activity.components;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentsUtils {
    public static List<String> list(String... strings) {
        return Arrays.stream(Arrays.stream(strings).toArray(String[]::new)).collect(Collectors.toList());
    }
}
