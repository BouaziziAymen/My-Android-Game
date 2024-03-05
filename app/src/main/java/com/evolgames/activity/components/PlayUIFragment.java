package com.evolgames.activity.components;

import static com.evolgames.activity.components.ComponentsUtils.dpToPixels;
import static com.evolgames.activity.components.ComponentsUtils.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evolgames.activity.ExpandableListViewAdaptor;
import com.evolgames.gameengine.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        createWeaponsListView(inflater, container);
        LinearLayout itemCreationLayout = leftLayout.findViewById(R.id.left_layout);
        itemCreationLayout.addView(itemsListView, dpToPixels(150,
                requireActivity().getResources()), LinearLayout.LayoutParams.WRAP_CONTENT);
        itemsListView.setVisibility(View.GONE);
        return fragment;
    }

    private void setupWeaponsButton(GameImageButton weaponsButton) {
        weaponsButton.setButtonType(GameImageButton.ButtonType.SELECT);
        weaponsButton.setImageResource(R.drawable.tools_icon);
        weaponsButton.setOnPressed(() -> itemsListView.setVisibility(View.VISIBLE));
        weaponsButton.setOnReleased(() -> itemsListView.setVisibility(View.GONE));
    }

    private void createWeaponsListView(LayoutInflater inflater, ViewGroup container) {
        itemsListView = (ExpandableListView) inflater.inflate(R.layout.expandable_list, container, false);
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
            Toast.makeText(this.getContext(), "Clicked: " + clickedItem, Toast.LENGTH_SHORT).show();
            return true;
        });
        ExpandableListViewAdaptor viewAdaptor = new ExpandableListViewAdaptor(inflater, listDataHeader, listHashMap);
        itemsListView.setAdapter(viewAdaptor);
    }


}