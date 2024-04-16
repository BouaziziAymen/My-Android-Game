package com.evolgames.activity.components;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class CustomArrayAdapter extends ArrayAdapter<CustomArrayAdapter.Item> {
    static class Item{
        int id;
        String title;
        String titleTranslated;
    }

    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Implement getView() method as needed
        // This method is responsible for creating a View for the item at the specified position.
        // Here you can inflate a layout for your item view and bind data to it.
        // For simplicity, you can just return convertView.
        return convertView;
    }
}