package com.evolgames.dollmutilationgame.activity.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.evolgames.dollmutilationgame.R;
import com.google.android.material.tabs.TabLayout;

public class CustomTabLayoutAdapter {

    private final TabLayout tabLayout;
    private final Context context;


    public CustomTabLayoutAdapter(Context context, TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        this.context = context;
    }
    public void setCustomViewInitial(TabLayout.Tab tab, int pos) {
        tab.setCustomView(null);
        ImageView tabIcon = new ImageView(context);
        if(pos==0){
            tabIcon.setImageResource(
                    R.drawable.ic_circle_full);
        } else {
            tabIcon.setImageResource(
                    R.drawable.ic_circle_empty);
        }
        tab.setCustomView(tabIcon);
    }
    public void setCustomView(TabLayout.Tab tab) {
        tab.setCustomView(null);
        ImageView tabIcon = new ImageView(context);

        tabIcon.setImageResource(tab.getPosition() == tabLayout.getSelectedTabPosition() ? R.drawable.ic_circle_full :
                    R.drawable.ic_circle_empty);
            tab.setCustomView(tabIcon);
    }
}
