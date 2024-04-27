package com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators;

import com.evolgames.dollmutilationgame.userinterface.view.shapes.points.PointImage;

import java.util.List;

public interface MovablesContainer {
    List<PointImage> getMovables(boolean moveLimits);
}
