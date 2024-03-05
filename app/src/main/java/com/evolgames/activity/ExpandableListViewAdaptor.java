package com.evolgames.activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.evolgames.gameengine.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewAdaptor extends BaseExpandableListAdapter {

    private final List<String> dataHeader;
    private final HashMap<String,List<String>> listHashMap;
    private final LayoutInflater inflater;

    public ExpandableListViewAdaptor(Context context, List<String> dataHeader, HashMap<String, List<String>> listHashMap) {
        this.dataHeader = dataHeader;
        this.listHashMap = listHashMap;
        inflater = LayoutInflater.from(context.getApplicationContext());
    }



    @Override
    public int getGroupCount() {
        return dataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(dataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(dataHeader.get(groupPosition)).get(childPosition);
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
        final String headerText = getGroup(groupPosition).toString();

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.exp_list_header, parent, false);

            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.questionTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(getGroup(groupPosition).toString());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.exp_list_body, parent, false);
            holder = new ViewHolder();

            holder.textView = convertView.findViewById(R.id.answerTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(getChild(groupPosition, childPosition).toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolder{
        TextView textView;
    }
}