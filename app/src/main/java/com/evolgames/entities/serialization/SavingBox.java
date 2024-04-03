package com.evolgames.entities.serialization;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.evolgames.activity.ResourceManager;
import com.evolgames.scenes.PlayScene;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SavingBox {

    private final PlayScene playScene;
    private final SerializationManager serializationManager;
    private int step;
    private int counter;
    private int retryCounter;
    private boolean lockedSave;
    private boolean savingErrorOnPause;

    public SavingBox(PlayScene playScene) {
        this.playScene = playScene;
        this.serializationManager = new SerializationManager();
        this.loadSelf();
    }

    public void setLockedSave(boolean lockedSave) {
        if (lockedSave && !this.lockedSave) {
            try {
                save("game_save.mut");
            } catch (Throwable t) {
                Log.e("Saving", "Error saving scene before action: " + t);
                this.savingErrorOnPause = true;
            }
        }
        this.lockedSave = lockedSave;
    }

    public void onStep() {
        step++;
        if (step % 600 == 0 && !lockedSave) {
            autoSave();
            counter = (counter < 9) ? counter + 1 : 0;
        }
    }

    public void onScenePause() {
        if (lockedSave) {
            return;
        }
        final String fileName = "game_save.mut";
        try {
            save(fileName);
            this.savingErrorOnPause = false;
            Log.i("Saving", "Saving game was successful");
        } catch (Throwable t) {
            this.savingErrorOnPause = true;
            Log.e("Saving", "Error saving scene on pause: " + t);
        }
    }

    public void onSceneResume() {
        lockedSave = false;
        if (!this.savingErrorOnPause) {
            try {
                load("game_save.mut");
            } catch (Throwable throwable) {
                Log.e("Saving", "Error loading saved scene line line 67: " + throwable);
                try {
                    loadLatestAutoSave();
                } catch (Throwable t) {
                    Log.e("Saving", "Error loading auto saved scene: " + t);
                }
            }
        } else {
            loadLatestAutoSave();
        }
    }

    private boolean loadAutoSave() {
        final String fileName = "regular_auto_save_" + counter;
        try {
            load(fileName);
            Log.i("Saving", "Loading latest auto save success: " + fileName);
            return true;
        } catch (Throwable t) {
            Log.i("Saving", "Loading latest auto save failure: " + t);
            return false;
        }
    }

    private void loadLatestAutoSave() throws RuntimeException {
        boolean success = false;
        while (!success && retryCounter < 9) {
            counter = (counter == 0) ? 9 : counter - 1;
            success = loadAutoSave();
            retryCounter++;
        }
        if (!success) {
            throw new RuntimeException("Error loading auto save");
        }
        retryCounter = 0;
    }

    private void autoSave() {
        final String fileName = "regular_auto_save_" + counter;
        try {
            save(fileName);
            Log.i("Saving", "Auto save success:" + fileName);
        } catch (Throwable t) {
            Log.e("Saving", "Error auto saving scene: " + t);
        }
    }

    private void load(String fileName) throws FileNotFoundException, RuntimeException {
        serializationManager.deserialize(playScene, fileName);
    }

    private void save(String fileName) throws FileNotFoundException, RuntimeException {
        serializationManager.serialize(playScene, fileName);
    }

    public void loadSelf() {
        SharedPreferences preferences =
                ResourceManager.getInstance()
                        .activity
                        .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
        this.counter = preferences.getInt("SAVING_BOX_COUNTER", 0);
        this.savingErrorOnPause = preferences.getBoolean("SAVING_ERROR_ON_PAUSE", false);
    }

    public void saveSelf() {
        SharedPreferences preferences =
                ResourceManager.getInstance()
                        .activity
                        .getSharedPreferences("RAG_DOLL_MUT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("SAVING_BOX_COUNTER", counter);
        editor.putBoolean("SAVING_ERROR_ON_PAUSE", savingErrorOnPause);
        editor.apply();
    }
}
