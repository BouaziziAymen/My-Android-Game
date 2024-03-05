package com.evolgames.activity;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.evolgames.activity.components.GameImageButton;
import com.evolgames.gameengine.R;
import com.evolgames.scenes.entities.SceneType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UIFacade {
    GameActivity activity;
    private ExpandableListView itemsListView;
    private LinearLayout itemCreationLayout;

    UIFacade(GameActivity activity){
        this.activity = activity;
    }

    public void onSceneChanged(SceneType sceneType){
        activity.runOnUiThread(() -> {
            switch (sceneType){
                case MAIN:
                    break;
                case MENU:
                    if(itemCreationLayout!=null)
                    itemCreationLayout.setVisibility(View.GONE);
                    break;
                case PLAY:
                    if(itemCreationLayout!=null)
                    itemCreationLayout.setVisibility(View.VISIBLE);
                    break;
                case EDITOR:
                    if(itemCreationLayout!=null)
                    itemCreationLayout.setVisibility(View.GONE);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sceneType);
            }
        });

    }
private void run(Runnable runnable){
    activity.runOnUiThread(runnable);
}
    public void createPlaySceneUi() {
        run(()-> {
          /*  LinearLayout leftLayout = (LinearLayout) View.inflate(activity,R.layout.left_layout,null);
            ConstraintLayout constraintLayout = activity.findViewById(R.id.main_constraint_layout);
            GameImageButton weaponsButton = leftLayout.findViewById(R.id.weapons_button);
            weaponsButton.setButtonType(GameImageButton.ButtonType.SELECT);
            weaponsButton.setImageResource(R.drawable.tools_icon);
            weaponsButton.setOnPressed(() -> itemsListView.setVisibility(View.VISIBLE));
            weaponsButton.setOnReleased(() -> itemsListView.setVisibility(View.GONE));
            createWeaponsListView();
            itemCreationLayout = leftLayout.findViewById(R.id.left_layout);
            itemCreationLayout.addView(itemsListView, dpToPixels(200), FrameLayout.LayoutParams.WRAP_CONTENT);
            itemsListView.setVisibility(View.GONE);
            constraintLayout.addView(leftLayout);*/
        });
    }

    private void createWeaponsListView() {
        itemsListView = (ExpandableListView) View.inflate(activity, R.layout.expandable_list, null);
        ArrayList<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<String>> listHashMap = new HashMap<>();

        listDataHeader.add("Swords");
        listDataHeader.add("Spears");
        listDataHeader.add("Guns");
        listDataHeader.add("Axes");
        listDataHeader.add("Grenades");
        listHashMap.put(listDataHeader.get(0), list("Roman sword", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber"));
        listHashMap.put(listDataHeader.get(1), list("Spear", "Harpoon", "Trident", "Javelin", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber"));
        listHashMap.put(listDataHeader.get(2), list("Ak47", "M16", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber"));
        listHashMap.put(listDataHeader.get(3), list("Axe", "Mace"));
        listHashMap.put(listDataHeader.get(4), list("Grenade", "Smoke grenade", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber", "Katana", "Falchion", "Saber"));
        itemsListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String clickedItem = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            Toast.makeText(activity.getApplicationContext(), "Clicked: " + clickedItem, Toast.LENGTH_SHORT).show();
            return true;
        });
        ExpandableListViewAdaptor viewAdaptor = new ExpandableListViewAdaptor(activity, listDataHeader, listHashMap);
        itemsListView.setAdapter(viewAdaptor);
    }

    private int dpToPixels(float dpValue) {
        float density = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    private List<String> list(String... strings) {
        return Arrays.stream(Arrays.stream(strings).toArray(String[]::new)).collect(Collectors.toList());
    }
}
