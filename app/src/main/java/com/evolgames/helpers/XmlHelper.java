package com.evolgames.helpers;

import static com.evolgames.entities.persistence.PersistenceCaretaker.XML_FOLDER;

import android.content.res.AssetManager;
import android.util.Xml;

import androidx.appcompat.app.AppCompatActivity;

import com.evolgames.userinterface.model.ItemCategory;
import com.evolgames.utilities.AssetUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class XmlHelper {
    AppCompatActivity activity;

    public XmlHelper(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static String convertToXmlFormat(String input) {
        // Replace spaces with underscores
        String processedString = input.replaceAll("\\s", "_");

        // Append ".xml" to the end
        processedString += ".xml";

        // Convert to lowercase
        processedString = processedString.toLowerCase();

        return processedString;
    }

    private List<String> readInternalStorageFiles() {
        List<String> fileNames = new ArrayList<>();
        File directory = activity.getFilesDir();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".xml")) {
                        fileNames.add(file.getName());
                    }
                }
            }
        }
        return fileNames;
    }

    public void fillItemsMapFromInternalStorage(Map<ItemCategory, List<ItemMetaData>> map) {
        List<String> fileNames = readInternalStorageFiles();
        for (String file : fileNames) {
            try {
                InputStream fis = activity.openFileInput(file);
                ItemMetaData metaData = parseXML(fis);
                if(metaData!=null) {
                    metaData.setUserCreated(true);
                    metaData.setFileName(file);
                    Objects.requireNonNull(map.get(metaData.getItemCategory())).add(metaData);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void fillItemsMapFromAssets(Map<ItemCategory, List<ItemMetaData>> map) {
        AssetManager assetManager = activity.getAssets();
        List<String> fileNames = AssetUtils.getAllFileNames(assetManager, XML_FOLDER);
        for (String file : fileNames) {
            try {
                InputStream fis = assetManager.open(String.format("%s/%s", XML_FOLDER, file));
                ItemMetaData metaData = parseXML(fis);
                metaData.setFileName(file);
                Objects.requireNonNull(map.get(metaData.getItemCategory())).add(metaData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ItemMetaData parseXML(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            return parseXML(parser);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private ItemMetaData parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagName.equals("tool")) {
                        String name = parser.getAttributeValue(0);
                        String category = parser.getAttributeValue(1);
                        ItemMetaData itemMetaData = new ItemMetaData();
                        itemMetaData.setToolName(name);
                        itemMetaData.setItemCategory(ItemCategory.fromName(category));
                        return itemMetaData;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    // Handle end tags
                    break;
                case XmlPullParser.TEXT:
                    // Handle text content
                    break;
            }
            eventType = parser.next();
        }
        return null;
    }
}
