package com.evolgames.activity.components;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.inputs.Button;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayUIFragment extends Fragment {
    private ExpandableListView itemsExpandableListView;
    private TouchHoldState touchHoldState = TouchHoldState.TOUCH;
    private GameImageButton usesButton, effectsButton;
    private GameImageButton touchHoldButton;
    private GameImageButton weaponsButton;
    private RecyclerView optionsRecyclerView;
    private OptionsListAdaptor optionsListAdaptor;
    private ExpandableListViewAdapter expandableListViewAdapter;
    private GameImageButton selectButton;
    private View lastClickedLayout;
    private View hintLayout;
    private ImageView hintIcon;
    private TextView hintText;
    private GameImageButton refreshButton;

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
        for (int i = 0; i < expandableListViewAdapter.getGroupCount(); i++) {
            if (itemsExpandableListView.isGroupExpanded(i)) {
                itemsExpandableListView.collapseGroup(i);
            }
        }
    }
    public void resetWeaponsButtonAndClose(){
        weaponsButton.setState(Button.State.NORMAL);
        itemsExpandableListView.setVisibility(View.GONE);
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

        GameImageButton trackCameraButton = leftLayout.findViewById(R.id.camera_button);
        setupCameraButton(trackCameraButton);

        GameImageButton homeButton = topLayout.findViewById(R.id.home_button);
        setupHomeButton(homeButton);

        selectButton = rightLayout.findViewById(R.id.select_button);
        setupSelectButton(selectButton);

        touchHoldButton = rightLayout.findViewById(R.id.touch_hold_button);
        setupTouchHoldButton(touchHoldButton);


        usesButton = rightLayout.findViewById(R.id.uses_button);
        setupUsesButton(usesButton);

        effectsButton = rightLayout.findViewById(R.id.effects_button);
        setupEffectsButton(effectsButton);

        refreshButton = topLayout.findViewById(R.id.refresh_button);
        setupRefreshButton(refreshButton);

        hintLayout = fragment.findViewById(R.id.hintLayout);
        hintIcon = hintLayout.findViewById(R.id.hintIcon);
        hintText = hintLayout.findViewById(R.id.hintText);

        GameImageButton helpButton = topLayout.findViewById(R.id.help_button);
        setupHelpButton(helpButton);

        this.itemsExpandableListView = leftLayout.findViewById(R.id.exp_list);
        this.itemsExpandableListView.setVisibility(View.GONE);
        setupWeaponsListView(inflater);

        optionsRecyclerView = rightLayout.findViewById(R.id.options_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); // Ensure vertical orientation
        optionsRecyclerView.setLayoutManager(layoutManager);
        optionsListAdaptor = new OptionsListAdaptor((optionButton, playerSpecialAction) -> ((GameActivity) requireActivity()).getUiController().onOptionSelected(playerSpecialAction));

        List<PlayerSpecialAction> imageList = new ArrayList<>();
        optionsListAdaptor.setPlayerSpecialActionList(imageList, null);
        optionsRecyclerView.setAdapter(optionsListAdaptor);
        return fragment;
    }

    private void setupCameraButton(GameImageButton trackCameraButton) {
        trackCameraButton.setOnPressed(() -> {
            ((GameActivity) getActivity()).getUiController().onTrackButtonClicked();
        });
        trackCameraButton.setOnReleased(() -> {
            ((GameActivity) getActivity()).getUiController().onTrackButtonReleased();
        });
    }


    private void setupMirrorButton(GameImageButton mirrorButton) {
        mirrorButton.setOnReleased(() -> ((GameActivity) getActivity()).getUiController().onMirrorButtonClicked());
    }

    public void setOptionsList(List<PlayerSpecialAction> optionsList, PlayerSpecialAction selectedAction) {
        if(optionsList.isEmpty()){
            optionsRecyclerView.setVisibility(View.INVISIBLE);
            usesButton.setState(Button.State.DISABLED);
            usesButton.setIcon(R.drawable.usages_icon_empty);
        } else {
            usesButton.setState(Button.State.PRESSED);
            usesButton.setIcon(R.drawable.usages_icon);
            optionsRecyclerView.setVisibility(View.VISIBLE);
        }
        optionsListAdaptor.setPlayerSpecialActionList(optionsList, selectedAction);
    }

    private void setupHelpButton(GameImageButton helpButton) {
        helpButton.setOnPressed(() -> {
            ((GameActivity) getActivity()).getUiController().onHelpPressed();
        });
    }
    private void setupUsesButton(GameImageButton usesButton) {
        usesButton.setOnReleased(() -> {
            optionsRecyclerView.setVisibility(View.GONE);
            ((GameActivity) getActivity()).getUiController().onUsesReleased();
        });
        usesButton.setOnPressed(() -> {
            effectsButton.setState(Button.State.NORMAL);
            optionsRecyclerView.setVisibility(View.VISIBLE);
            ((GameActivity) getActivity()).getUiController().onUsesClicked();
        });
    }

    private void setupRefreshButton(GameImageButton refreshButton) {
        refreshButton.setOnPressed(() -> {
            ((GameActivity) getActivity()).getUiController().onRefreshButtonClicked();
        });
    }


    private void setupEffectsButton(GameImageButton effectsButton) {
        effectsButton.setOnReleased(() -> {
            optionsRecyclerView.setVisibility(View.GONE);
            ((GameActivity) getActivity()).getUiController().onEffectsReleased();
        });
        effectsButton.setOnPressed(() -> {
            optionsRecyclerView.setVisibility(View.VISIBLE);
            usesButton.setState(Button.State.NORMAL);
            ((GameActivity) getActivity()).getUiController().onEffectsClicked();
        });
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
        listDataHeader.removeIf(e->e.nonCreatable);
        listDataHeader.sort(Comparator.comparing(ItemCategory::name));
        this.expandableListViewAdapter = new ExpandableListViewAdapter(this,inflater, listDataHeader, map,itemsExpandableListView);
        itemsExpandableListView.setAdapter(expandableListViewAdapter);

        // Set the onChildClickListener
        itemsExpandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // Reset the color of the previously clicked item if any
            resetClickedItem();

            // Store the current clicked layout
            LinearLayout clickedLayout = v.findViewById(R.id.itemsElementLayout);

            // Change the color of the clicked layout
            clickedLayout.setBackgroundResource(R.drawable.bg_items_element_pressed);

            // Store the current clicked layout
            lastClickedLayout = clickedLayout;

            ItemMetaData clickedItem = (ItemMetaData) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            ResourceManager.getInstance().activity.getUiController().onItemButtonPressed(clickedItem);
            return true;
        });
    }


    public void resetClickedItem() {
        if(lastClickedLayout!=null) {
            // Reset the color of the clicked item
            lastClickedLayout.setBackgroundResource(R.drawable.bg_items_element);
            // Reset the last clicked layout
            lastClickedLayout = null;
        }
    }


    public void resetSelectButton() {
        selectButton.setState(Button.State.NORMAL);
        ResourceManager.getInstance().activity.getUiController().onTouchHoldButtonSwitched(touchHoldState);
    }

    public enum TouchHoldState {
        TOUCH, SELECT, HOLD
    }



    public void showHint(String text, int iconId){
        hintLayout.setVisibility(View.VISIBLE);
        hintText.setText(text);
        hintIcon.setImageResource(iconId);
        final long durationInMilliseconds = 3000L;

        // Duration for fade in and fade out animations
        final long animationDuration = 1000L;

        // Start fade in animation
        startFadeInAnimation(animationDuration);

        // Schedule fade out animation after the specified duration
        new Handler().postDelayed(() -> startFadeOutAnimation(animationDuration), durationInMilliseconds);
    }

    private void startFadeInAnimation(long duration) {
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0f, 1f);
        fadeInAnimation.setDuration(duration);
        hintLayout.startAnimation(fadeInAnimation);
    }

    private void startFadeOutAnimation(long duration) {
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1f, 0f);
        fadeOutAnimation.setDuration(duration);

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Hide the LinearLayout after the fade out animation
                hintLayout.setVisibility(LinearLayout.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        hintLayout.startAnimation(fadeOutAnimation);
    }

}