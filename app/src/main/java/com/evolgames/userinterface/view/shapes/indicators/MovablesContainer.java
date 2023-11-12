package com.evolgames.userinterface.view.shapes.indicators;

import com.evolgames.userinterface.view.shapes.points.PointImage;
import java.util.List;

public interface MovablesContainer {
  List<PointImage> getMovables(boolean moveLimits);
}
