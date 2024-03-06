package com.evolgames.activity.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.R;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.userinterface.model.ItemCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayUIFragment extends Fragment {
    private ExpandableListView itemsListView;

    public PlayUIFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.play_ui_fragment, container, false);
        View leftLayout = fragment.findViewById(R.id.left_layout);
        GameImageButton weaponsButton = leftLayout.findViewById(R.id.weapons_button);
        setupWeaponsButton(weaponsButton);
        this.itemsListView = leftLayout.findViewById(R.id.exp_list);
        setupWeaponsListView(inflater);
        itemsListView.setVisibility(View.GONE);
        return fragment;
    }

    private void setupWeaponsButton(GameImageButton weaponsButton) {
        weaponsButton.setButtonType(GameImageButton.ButtonType.SELECT);
        weaponsButton.setIcon(R.drawable.tools_icon);
        weaponsButton.setOnPressed(() -> itemsListView.setVisibility(View.VISIBLE));
        weaponsButton.setOnReleased(() -> itemsListView.setVisibility(View.GONE));
    }

    private void setupWeaponsListView(LayoutInflater inflater) {
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemCategory> listDataHeader = new ArrayList<>(ResourceManager.getInstance().getItemsMap().keySet());
        ExpandableListViewAdaptor viewAdaptor = new ExpandableListViewAdaptor(inflater, listDataHeader, map);
        itemsListView.setAdapter(viewAdaptor);

        itemsListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            ItemMetaData clickedItem = (ItemMetaData) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            //Toast.makeText(this.getContext(), "Clicked: " + clickedItem.getToolName(), Toast.LENGTH_SHORT).show();
            ResourceManager.getInstance().activity.getUiController().onItemButtonPressed(clickedItem);
            return true;
        });
    }

}