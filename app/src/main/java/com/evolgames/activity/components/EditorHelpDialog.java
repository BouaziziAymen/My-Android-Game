package com.evolgames.activity.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.evolgames.gameengine.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EditorHelpDialog extends Dialog {
    private final int[] images = {R.drawable.scenarioeditor1, R.drawable.scenarioeditor2, R.drawable.scenarioeditor3,R.drawable.scenarioeditor4,R.drawable.scenarioeditor5,R.drawable.scenarioeditor6};

    public EditorHelpDialog(@NonNull Context context) {
        super(context);
    }

    // Additionally, use the mirror button to flip an item.
    private final String[] helpMessages = {
            "To start, click the pencil button and add a body to your design.",
            "Connect bodies with joints. There are four types: revolute, elastic, weld, and prismatic. Remember to configure each joint.",
            "Customize the physical properties of each layer using the layer settings.",
            "Enhance your layers by decorating them with graphics that have no physical impact.",
            "Design a gun by creating a projectile indicator, connecting it to a casing indicator to define projectile behavior, and then linking both to a shooting usage type.",
            "Incorporate images into your design and adjust their size or orientation as needed. Use the pipe button to extract colors from the image."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_dialog);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ImagePagerAdapter adapter = new ImagePagerAdapter(getContext(),images);
        viewPager.setAdapter(adapter);

        TextView textView = findViewById(R.id.helpText);

        CustomTabLayoutAdapter customTabLayoutAdapter = new CustomTabLayoutAdapter(getContext(), tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // You can set custom titles for each tab if needed
                    customTabLayoutAdapter.setCustomViewInitial(tab,position);
                    textView.setText(helpMessages[position]);
                }).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                customTabLayoutAdapter.setCustomView(tab);
                textView.setText(helpMessages[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                customTabLayoutAdapter.setCustomView(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).select();
        textView.setText(helpMessages[0]);
    }

}

