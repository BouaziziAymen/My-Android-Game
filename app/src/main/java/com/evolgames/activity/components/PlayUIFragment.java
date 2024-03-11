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
import com.evolgames.gameengine.R;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.ItemCategory;
import com.evolgames.userinterface.view.inputs.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayUIFragment extends Fragment {
    private ExpandableListView itemsListView;
    private TouchHoldState touchHoldState = TouchHoldState.TOUCH;
    private GameImageButton usesButton;
    private RecyclerView optionsRecyclerView;
    private OptionsListAdaptor optionsListAdaptor;

    public enum TouchHoldState{
        TOUCH,HOLD
    }

    public PlayUIFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.play_ui_fragment, container, false);
        View leftLayout = fragment.findViewById(R.id.left_layout);
        View topLayout = fragment.findViewById(R.id.top_layout);
        View rightLayout = fragment.findViewById(R.id.right_layout);
        GameImageButton weaponsButton = leftLayout.findViewById(R.id.weapons_button);
        setupWeaponsButton(weaponsButton);
        GameImageButton homeButton = topLayout.findViewById(R.id.home_button);
        setupHomeButton(homeButton);
        GameImageButton touchHoldButton = rightLayout.findViewById(R.id.touch_hold_button);
        setupTouchHoldButton(touchHoldButton);
        usesButton = rightLayout.findViewById(R.id.uses_button);
        setupUsesButton(usesButton);

        this.itemsListView = leftLayout.findViewById(R.id.exp_list);
        setupWeaponsListView(inflater);

        optionsRecyclerView = rightLayout.findViewById(R.id.options_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); // Ensure vertical orientation
        optionsRecyclerView.setLayoutManager(layoutManager);
        optionsListAdaptor = new OptionsListAdaptor((optionButton, playerSpecialAction) -> ((GameActivity)getActivity()).getUiController().onOptionSelected(playerSpecialAction));

        List<PlayerSpecialAction> imageList = new ArrayList<>();
        optionsListAdaptor.setPlayerSpecialActionList(imageList,null );
        optionsRecyclerView.setAdapter(optionsListAdaptor);
        optionsRecyclerView.setVisibility(View.GONE);

        itemsListView.setVisibility(View.GONE);
        return fragment;
    }

    public void setOptionsList(List<PlayerSpecialAction> optionsList, PlayerSpecialAction selectedAction){
            optionsListAdaptor.setPlayerSpecialActionList(optionsList,selectedAction);
    }
    private void setupUsesButton(GameImageButton usesButton) {
        usesButton.setVisibility(View.INVISIBLE);
        usesButton.setOnReleased(()->optionsRecyclerView.setVisibility(View.GONE));
        usesButton.setOnPressed(()->optionsRecyclerView.setVisibility(View.VISIBLE));
    }

    private void setupTouchHoldButton(GameImageButton touchHoldButton) {
        touchHoldButton.setOnReleased(()->{
            if(touchHoldState==TouchHoldState.HOLD){
                touchHoldState = TouchHoldState.TOUCH;
                touchHoldButton.setIcon(R.drawable.drag_icon);
                usesButton.setState(Button.State.NORMAL);
            } else {
                touchHoldState = TouchHoldState.HOLD;
                touchHoldButton.setIcon(R.drawable.grab_icon);
            }
            if(touchHoldState==TouchHoldState.HOLD){
               showUsesButton();
            } else {
             hideUsesButton();
            }
            ResourceManager.getInstance().activity.getUiController().onTouchHoldButtonSwitched(touchHoldState);
        });
    }
    void showUsesButton(){
        usesButton.setVisibility(View.VISIBLE);
    }
    void hideUsesButton(){
        usesButton.setVisibility(View.INVISIBLE);
        optionsRecyclerView.setVisibility(View.GONE);
    }

    private void setupWeaponsButton(GameImageButton weaponsButton) {
        weaponsButton.setOnPressed(() -> itemsListView.setVisibility(View.VISIBLE));
        weaponsButton.setOnReleased(() -> itemsListView.setVisibility(View.GONE));
    }
    private void setupHomeButton(GameImageButton homeButton) {
        homeButton.setOnReleased(() ->ResourceManager.getInstance().activity.getUiController().onHomeButtonPressed());
    }

    private void setupWeaponsListView(LayoutInflater inflater) {
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemCategory> listDataHeader = new ArrayList<>(ResourceManager.getInstance().getItemsMap().keySet());
        ExpandableListViewAdaptor viewAdaptor = new ExpandableListViewAdaptor(inflater, listDataHeader, map);
        itemsListView.setAdapter(viewAdaptor);

        itemsListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            ItemMetaData clickedItem = (ItemMetaData) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            ResourceManager.getInstance().activity.getUiController().onItemButtonPressed(clickedItem);
            return true;
        });
    }

}