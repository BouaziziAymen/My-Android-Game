package com.evolgames.dollmutilationgame.activity.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.fragment.app.DialogFragment;

import com.evolgames.dollmutilationgame.activity.GameActivity;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.R;

public class SettingsDialog extends DialogFragment {

    private boolean newSound;
    private boolean newVibration;
    private boolean newMusic;

    private boolean newHints;
    private String newMap;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        this.newSound = ResourceManager.getInstance().isSound();
        this.newMusic = ResourceManager.getInstance().isMusic();
        this.newMap = ResourceManager.getInstance().getMapString();
        this.newHints = ResourceManager.getInstance().isHints();
        this.newVibration = ResourceManager.getInstance().isVibration();

        View dialogLayout = inflater.inflate(R.layout.options_dialog, null);
         CheckBox soundCheckBox = dialogLayout.findViewById(R.id.soundCheckbox);
         CheckBox musicCheckBox = dialogLayout.findViewById(R.id.musicCheckbox);
        CheckBox hintsCheckBox = dialogLayout.findViewById(R.id.hintsCheckbox);
        CheckBox vibrationCheckbox = dialogLayout.findViewById(R.id.vibrationCheckbox);

         RadioButton woodOptionCheckBox = dialogLayout.findViewById(R.id.woodMapOption);
        RadioButton openOptionCheckBox = dialogLayout.findViewById(R.id.openMapOption);
        RadioButton marbleOptionCheckBox = dialogLayout.findViewById(R.id.marbleMapOption);

        soundCheckBox.setChecked(ResourceManager.getInstance().isSound());
        musicCheckBox.setChecked(ResourceManager.getInstance().isMusic());
        hintsCheckBox.setChecked(ResourceManager.getInstance().isHints());
        vibrationCheckbox.setChecked(ResourceManager.getInstance().isVibration());
        String mapString = ResourceManager.getInstance().getMapString();
        woodOptionCheckBox.setChecked(mapString.equals("Wood"));
        openOptionCheckBox.setChecked(mapString.equals("Open"));
        marbleOptionCheckBox.setChecked(mapString.equals("Marble"));

        soundCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->{newSound = isChecked;});
        musicCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->{newMusic = isChecked;});
        hintsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->{newHints = isChecked;});
        vibrationCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->{newVibration = isChecked;});

        woodOptionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {if(isChecked){this.newMap = "Wood";}});
        openOptionCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {if(isChecked){this.newMap = "Open";}});
        marbleOptionCheckBox.setOnCheckedChangeListener((buttonView, isChecked)-> {if(isChecked){this.newMap = "Marble";}});

        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton(R.string.save_settings, (d, id) -> {
                   ResourceManager.getInstance().setMusic(newMusic);
                   ResourceManager.getInstance().setSound(newSound);
                   ResourceManager.getInstance().setHints(newHints);
                    ResourceManager.getInstance().setVibration(newVibration);
                   ResourceManager.getInstance().setMapString(newMap);
                    ((GameActivity)getActivity()).saveOptions();
                    ((GameActivity) requireActivity()).installMenuUi();
                }).setNegativeButton(R.string.cancel, (d, id) -> {
                    SettingsDialog.this.getDialog().cancel();
                    ((GameActivity) requireActivity()).installMenuUi();
                });



        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        alertDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                SettingsDialog.this.getDialog().cancel();
                ((GameActivity) getActivity()).installMenuUi();
                return true;
            }
            return false;
        });

        return alertDialog;
    }
}
