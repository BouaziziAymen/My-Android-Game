package com.evolgames.entities.persistence;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.factories.MaterialFactory;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ItemCategory;
import com.evolgames.userinterface.model.LayerModel;
import com.evolgames.userinterface.model.ToolModel;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class VersioningHelper {
    @FunctionalInterface
    public interface LayerTransform{

        void transform(LayerModel layerModel);
    }
    @FunctionalInterface
    public interface ToolTransform{

        void transform(ToolModel toolModel);
    }
    public static void applyTreatment(ToolTransform transform){
        PersistenceCaretaker persistenceCaretaker = PersistenceCaretaker.getInstance();
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemMetaData> items = map.values().stream().flatMap(List::stream).collect(Collectors.toList());
       for(ItemMetaData item:items) {
           try {
               ToolModel toolModel = persistenceCaretaker.loadToolModel(item.getFileName(),false,!item.isUserCreated());
                  transform.transform(toolModel);
                  persistenceCaretaker.saveToolModel(toolModel,item.getFileName(),"version\\");
           } catch (IOException | PersistenceException | SAXException |
                    ParserConfigurationException | TransformerException e) {
               throw new RuntimeException(e);
           }

       }

    }


    public static void applyTreatmentToLayers(LayerTransform layerTransform){
        VersioningHelper.applyTreatment((toolModel) -> {
            for(BodyModel bodyModel:toolModel.getBodies()){
                for(LayerModel layerModel:bodyModel.getLayers()){
                   layerTransform.transform(layerModel);
                }
            }
        });
    }








}
