package com.evolgames.activity.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.evolgames.activity.ExpandableListViewAdaptor;
import com.evolgames.gameengine.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GameExpandableListView extends ExpandableListView {
    private ArrayList<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    public GameExpandableListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        ExpandableListView itemsListView = findViewById(R.id.exp_list);
        fillData();
        ExpandableListViewAdaptor viewAdaptor = new ExpandableListViewAdaptor(this.getContext(),listDataHeader,listHashMap);
        itemsListView.setAdapter(viewAdaptor);
        itemsListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String clickedItem = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
            Toast.makeText(this.getContext(), "Clicked: " + clickedItem, Toast.LENGTH_SHORT).show();
            return true;
    });

}
    public void fillData()
    {
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();

        listDataHeader.add("Swords");
        listDataHeader.add("Spears");
        listDataHeader.add("Guns");
        listDataHeader.add("Axes");
        listDataHeader.add("Grenades");
        listHashMap.put(listDataHeader.get(0),list("Roman sword","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber"));
        listHashMap.put(listDataHeader.get(1),list("Spear","Harpoon","Trident","Javelin","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber"));
        listHashMap.put(listDataHeader.get(2),list("Ak47","M16","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber"));
        listHashMap.put(listDataHeader.get(3),list("Axe","Mace"));
        listHashMap.put(listDataHeader.get(4),list("Grenade","Smoke grenade","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber","Katana","Falchion","Saber"));
    }
    private List<String> list(String...strings){
        return Arrays.stream(Arrays.stream(strings).toArray(String[]::new)).collect(Collectors.toList());
    }
}