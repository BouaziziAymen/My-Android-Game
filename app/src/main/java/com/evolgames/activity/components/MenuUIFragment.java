package com.evolgames.activity.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evolgames.activity.GameActivity;
import com.evolgames.gameengine.R;

public class MenuUIFragment extends Fragment {

    public MenuUIFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.menu_ui_fragment, container, false);


        GameImageButton settingsButton = fragment.findViewById(R.id.options_button);
        settingsButton.setOnReleased(() -> {
            ((GameActivity) requireActivity()).showOptionsDialog();
        });


        GameImageButton rateButton = fragment.findViewById(R.id.rate_button);
        rateButton.setOnReleased(() -> {
            ((GameActivity) requireActivity()).showRateUsDialog();;
        });


        ImageView logo = fragment.findViewById(R.id.logo);
       // Create a fade-in animation
        Animation fadeInAnimation = new AlphaAnimation(0f, 1f);
        fadeInAnimation.setDuration(1000);
        fadeInAnimation.setStartOffset(4000);

        // Apply the animation to the ImageView
        logo.startAnimation(fadeInAnimation);


        GameImageButton editorButton = fragment.findViewById(R.id.editor_button);
        editorButton.setOnReleased(() -> ((GameActivity) requireActivity()).getUiController().onEditorClicked());
        ImageView mottoView = fragment.findViewById(R.id.motto);
        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out);
        mottoView.startAnimation(fadeIn);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mottoView.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Hide the ImageView when fade-out animation ends
                mottoView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        return fragment;
    }

}

