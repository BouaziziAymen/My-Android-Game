package com.evolgames.utilities;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetUtils {

        // Function to get all file names in the assets folder
        public static List<String> getAllFileNames(AssetManager assetManager, String folderName) {
            List<String> fileNames = new ArrayList<>();
            try {
                // List all files and folders in the assets folder
                String[] files = assetManager.list(folderName);
                if (files != null) {
                    // Add each file name to the list
                    fileNames.addAll(Arrays.asList(files));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileNames;
        }

}
