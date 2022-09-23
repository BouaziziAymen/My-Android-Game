package com.evolgames.helpers.utilities;

import com.evolgames.entities.persistence.PersistenceCaretaker;
import com.evolgames.gameengine.GameActivity;
import com.evolgames.userinterface.model.ToolModel;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

public class ToolUtils {

    public static List<String> getToolNamesByCategory(GameActivity activity, String categoryPrefix){
        return Arrays.stream(activity.fileList()).filter(f->f.startsWith(categoryPrefix)).collect(Collectors.toList());
    }
    public static ToolModel getProjectileModel(String fileName){
            try {
               return PersistenceCaretaker.getInstance().loadToolModel(fileName);
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                return null;
            }
        }


}
