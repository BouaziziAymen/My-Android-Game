package com.evolgames.entities.blockvisitors.utilities;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.persistence.PersistenceCaretaker;
import com.evolgames.entities.persistence.PersistenceException;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class ToolUtils {

  public static List<String> getToolNamesByCategory(GameActivity activity, String categoryPrefix) {
    return Arrays.stream(activity.fileList())
        .filter(f -> f.startsWith(categoryPrefix))
        .collect(Collectors.toList());
  }

  public static ToolModel getProjectileModel(String fileName)
      throws PersistenceException, IOException, ParserConfigurationException, SAXException {
    return PersistenceCaretaker.getInstance().loadToolModel(fileName,false);
  }

  public static float getAxisExtent(ToolModel toolModel, Vector2 axis) {
    float max = -Float.MAX_VALUE;
    float min = Float.MAX_VALUE;
    for (BodyModel bodyModel : toolModel.getBodies()) {
      for (LayerModel layerModel : bodyModel.getLayers()) {
        for (Vector2 v : layerModel.getPoints()) {
          float dot = v.dot(axis);
          max = Math.max(dot, max);
          min = Math.min(dot, min);
        }
      }
    }
    return max - min;
  }
}
