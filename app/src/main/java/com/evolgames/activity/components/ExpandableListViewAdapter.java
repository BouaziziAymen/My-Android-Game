package com.evolgames.activity.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.evolgames.gameengine.R;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.userinterface.model.ItemCategory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private final List<ItemCategory> dataHeader;
    private final Map<ItemCategory, List<ItemMetaData>> listHashMap;
    private final LayoutInflater inflater;
    private final ExpandableListView listView;
    private int lastExpandedGroupPosition = -1;

    public ExpandableListViewAdapter(LayoutInflater inflater, List<ItemCategory> dataHeader, Map<ItemCategory, List<ItemMetaData>> listHashMap, ExpandableListView itemsExpandableListView) {
        this.dataHeader = dataHeader;
        this.listHashMap = listHashMap;
        this.inflater = inflater;
        this.listView = itemsExpandableListView;
    }

    @Override
    public int getGroupCount() {
        return dataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(listHashMap.get(dataHeader.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(listHashMap.get(dataHeader.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.exp_list_header, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.itemsSectionTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemCategory itemCategory = dataHeader.get(groupPosition);
        holder.textView.setText(inflater.getContext().getString(itemCategory.nameId));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.exp_list_body, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.itemsElementTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemCategory itemCategory = dataHeader.get(groupPosition);
        ItemMetaData itemMetaData = Objects.requireNonNull(listHashMap.get(itemCategory)).get(childPosition);
        holder.textView.setText(itemMetaData.getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        // Collapse the last expanded group
        if (lastExpandedGroupPosition != -1 && groupPosition != lastExpandedGroupPosition) {
           listView.collapseGroup(lastExpandedGroupPosition);
        }
        lastExpandedGroupPosition = groupPosition;
        super.onGroupExpanded(groupPosition);
    }

    private static class ViewHolder {
        TextView textView;
    }
}
