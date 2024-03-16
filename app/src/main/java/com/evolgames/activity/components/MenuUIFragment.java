package com.evolgames.activity.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evolgames.activity.GameActivity;
import com.evolgames.gameengine.R;

public class MenuUIFragment extends Fragment {

    public MenuUIFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.menu_ui_fragment, container, false);
        GameImageButton editorButton = fragment.findViewById(R.id.editor_button);
        editorButton.setOnReleased(()->{
            ((GameActivity)getActivity()).getUiController().onEditorClicked();
        });
        return fragment;
    }

}

