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

public class HelpDialog extends Dialog {
    private final int[] images = {R.drawable.scenario1, R.drawable.scenario2,R.drawable.scenario3, R.drawable.scenario4,R.drawable.scenario5,R.drawable.scenario6,R.drawable.scenario7,R.drawable.scenario8,R.drawable.scenario9};

    public HelpDialog(@NonNull Context context) {
        super(context);
    }

// Additionally, use the mirror button to flip an item.
private final String[] helpMessages = new String[]{
       getContext().getString(R.string.scenario_help_1),
        getContext().getString(R.string.scenario_help_2),
        getContext().getString(R.string.scenario_help_3),
        getContext().getString(R.string.scenario_help_4),
        getContext().getString(R.string.scenario_help_5),
        getContext().getString(R.string.scenario_help_6),
        getContext().getString(R.string.scenario_help_7),
        getContext().getString(R.string.scenario_help_8),
        getContext().getString(R.string.scenario_help_9)
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

