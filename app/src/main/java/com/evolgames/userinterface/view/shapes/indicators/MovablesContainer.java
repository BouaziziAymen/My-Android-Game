package com.evolgames.userinterface.view.shapes.indicators;

import com.evolgames.userinterface.view.shapes.points.PointImage;

import java.util.ArrayList;

public interface MovablesContainer {
    ArrayList<PointImage> getMovables(boolean moveLimits);
}
