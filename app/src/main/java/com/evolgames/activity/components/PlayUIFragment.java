package com.evolgames.activity.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evolgames.activity.GameActivity;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.hand.PlayerSpecialAction;
import com.evolgames.gameengine.R;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.userinterface.model.ItemCategory;
import com.evolgames.userinterface.view.inputs.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayUIFragment extends Fragment {
    private ExpandableListView itemsExpandableListView;
    private TouchHoldState touchHoldState = TouchHoldState.TOUCH;
    private GameImageButton usesButton;
    private GameImageButton touchHoldButton;
    private GameImageButton weaponsButton;
    private RecyclerView optionsRecyclerView;
    private OptionsListAdaptor optionsListAdaptor;
    private ExpandableListViewAdaptor expandableListViewAdaptor;
    private GameImageButton selectButton;

    public PlayUIFragment() {
        // Required empty public constructor
    }

    private void resetTouchHold() {
        touchHoldButton.setState(Button.State.NORMAL);
        touchHoldState = TouchHoldState.TOUCH;
        touchHoldButton.setIcon(R.drawable.drag_icon);
        usesButton.setState(Button.State.NORMAL);
    }

    public void reset() {
        resetTouchHold();
        optionsListAdaptor.setPlayerSpecialActionList(new ArrayList<>(), null);
        optionsRecyclerView.setVisibility(View.GONE);
        itemsExpandableListView.setVisibility(View.GONE);
        weaponsButton.setState(Button.State.NORMAL);
        for (int i = 0; i < expandableListViewAdaptor.getGroupCount(); i++) {
            if (itemsExpandableListView.isGroupExpanded(i)) {
                itemsExpandableListView.collapseGroup(i);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.play_ui_fragment, container, false);
        View leftLayout = fragment.findViewById(R.id.left_layout);
        View topLayout = fragment.findViewById(R.id.top_layout);
        View rightLayout = fragment.findViewById(R.id.right_layout);
        weaponsButton = leftLayout.findViewById(R.id.weapons_button);
        setupWeaponsButton(weaponsButton);
        GameImageButton mirrorButton = leftLayout.findViewById(R.id.mirror_button);
        setupMirrorButton(mirrorButton);

        GameImageButton homeButton = topLayout.findViewById(R.id.home_button);
        setupHomeButton(homeButton);

        selectButton = rightLayout.findViewById(R.id.select_button);
        setupSelectButton(selectButton);

        touchHoldButton = rightLayout.findViewById(R.id.touch_hold_button);
        setupTouchHoldButton(touchHoldButton);


        usesButton = rightLayout.findViewById(R.id.uses_button);
        setupUsesButton(usesButton);

        this.itemsExpandableListView = leftLayout.findViewById(R.id.exp_list);
        this.itemsExpandableListView.setVisibility(View.GONE);
        setupWeaponsListView(inflater);

        optionsRecyclerView = rightLayout.findViewById(R.id.options_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); // Ensure vertical orientation
        optionsRecyclerView.setLayoutManager(layoutManager);
        optionsListAdaptor = new OptionsListAdaptor((optionButton, playerSpecialAction) -> ((GameActivity) getActivity()).getUiController().onOptionSelected(playerSpecialAction));

        List<PlayerSpecialAction> imageList = new ArrayList<>();
        optionsListAdaptor.setPlayerSpecialActionList(imageList, null);
        optionsRecyclerView.setAdapter(optionsListAdaptor);
        optionsRecyclerView.setVisibility(View.GONE);
        return fragment;
    }


    private void setupMirrorButton(GameImageButton mirrorButton) {
        mirrorButton.setOnReleased(() -> {
            ((GameActivity) getActivity()).getUiController().onMirrorButtonClicked();
        });
    }

    public void setOptionsList(List<PlayerSpecialAction> optionsList, PlayerSpecialAction selectedAction) {
        optionsListAdaptor.setPlayerSpecialActionList(optionsList, selectedAction);
    }

    private void setupUsesButton(GameImageButton usesButton) {
        usesButton.setOnReleased(() -> optionsRecyclerView.setVisibility(View.GONE));
        usesButton.setOnPressed(() -> optionsRecyclerView.setVisibility(View.VISIBLE));
    }

    private void setupSelectButton(GameImageButton selectButton) {
        selectButton.setOnPressed(() -> {
            ResourceManager.getInstance().activity.getUiController().onTouchHoldButtonSwitched(TouchHoldState.SELECT);
        });
        selectButton.setOnReleased(() -> {
            ResourceManager.getInstance().activity.getUiController().onTouchHoldButtonSwitched(touchHoldState);
        });
    }

    private void setupTouchHoldButton(GameImageButton touchHoldButton) {
        touchHoldButton.setOnReleased(() -> {
            if (touchHoldState == TouchHoldState.HOLD) {
                touchHoldState = TouchHoldState.TOUCH;
                touchHoldButton.setIcon(R.drawable.drag_icon);
                usesButton.setState(Button.State.NORMAL);
            } else {
                touchHoldState = TouchHoldState.HOLD;
                touchHoldButton.setIcon(R.drawable.grab_icon);
            }
            ResourceManager.getInstance().activity.getUiController().onTouchHoldButtonSwitched(touchHoldState);
        });
    }


    private void setupWeaponsButton(GameImageButton weaponsButton) {
        weaponsButton.setOnPressed(() -> itemsExpandableListView.setVisibility(View.VISIBLE));
        weaponsButton.setOnReleased(() -> itemsExpandableListView.setVisibility(View.GONE));
    }

    private void setupHomeButton(GameImageButton homeButton) {
        homeButton.setOnReleased(() -> ResourceManager.getInstance().activity.getUiController().onHomeButtonPressed());
    }

    private void setupWeaponsListView(LayoutInflater inflater) {
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemCategory> listDataHeader = new ArrayList<>(ResourceManager.getInstance().getItemsMap().keySet());
        this.expandableListViewAdaptor = new ExpandableListViewAdaptor(inflater, listDataHeader, map);
        itemsExpandableListView.setAdapter(expandableListViewAdaptor);

        itemsExpandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            ItemMetaData clickedItem = (ItemMetaData) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            ResourceManager.getInstance().activity.getUiController().onItemButtonPressed(clickedItem);
            return true;
        });
    }

    public void resetSelectButton() {
        selectButton.setState(Button.State.NORMAL);
        ResourceManager.getInstance().activity.getUiController().onTouchHoldButtonSwitched(touchHoldState);
    }

    public enum TouchHoldState {
        TOUCH, SELECT, HOLD
    }

}