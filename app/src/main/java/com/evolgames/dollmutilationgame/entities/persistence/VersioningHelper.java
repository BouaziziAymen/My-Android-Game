package com.evolgames.dollmutilationgame.entities.persistence;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.helpers.ItemMetaData;
import com.evolgames.dollmutilationgame.helpers.XmlHelper;
import com.evolgames.dollmutilationgame.userinterface.model.BodyModel;
import com.evolgames.dollmutilationgame.userinterface.model.ItemCategory;
import com.evolgames.dollmutilationgame.userinterface.model.LayerModel;
import com.evolgames.dollmutilationgame.userinterface.model.ToolModel;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class VersioningHelper {
    public static void applyTreatment(ToolTransform transform, Predicate<ItemMetaData> condition) {
        PersistenceCaretaker persistenceCaretaker = PersistenceCaretaker.getInstance();
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemMetaData> items = map.values().stream().flatMap(List::stream).filter(condition).collect(Collectors.toList());
        for (ItemMetaData item : items) {
            try {
                ToolModel toolModel = persistenceCaretaker.loadToolModel(item.getFileName(), true, !item.isUserCreated());
                transform.transform(toolModel);
                persistenceCaretaker.saveToolModel(toolModel,  XmlHelper.convertToXmlFormat(toolModel.getProperties().getToolName()), "version\\");
            } catch (IOException | PersistenceException | SAXException |
                     ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public static void applyTreatmentToLayers(LayerTransform layerTransform, Predicate<ItemMetaData> condition) {
        VersioningHelper.applyTreatment((toolModel) -> {
            for (BodyModel bodyModel : toolModel.getBodies()) {
                for (LayerModel layerModel : bodyModel.getLayers()) {
                    layerTransform.transform(layerModel);
                }
            }
        }, condition);
    }

    @FunctionalInterface
    public interface LayerTransform {

        void transform(LayerModel layerModel);
    }


    @FunctionalInterface
    public interface ToolTransform {

        void transform(ToolModel toolModel);
    }


}
