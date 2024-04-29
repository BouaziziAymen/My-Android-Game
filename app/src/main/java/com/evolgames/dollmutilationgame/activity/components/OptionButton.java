package com.evolgames.dollmutilationgame.activity.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.dollmutilationgame.R;

public class OptionButton extends FrameLayout {
    private ImageView button;
    private ImageView icon;
    private Button.State state;
    private boolean oneClick;

    public OptionButton(Context context) {
        super(context);
        init();
    }

    public OptionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OptionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.option_button, this, true);
        button = findViewById(R.id.image);
        icon = findViewById(R.id.icon);
    }

    public void setOneClick(boolean oneClick) {
        this.oneClick = oneClick;
    }

    public void setIcon(int resourceId) {
        icon.setImageResource(resourceId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!oneClick) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (state == Button.State.NORMAL) {
                        button.setImageResource(R.drawable.warp_selected);
                        rotateAnimation();
                        this.state = Button.State.PRESSED;
                    }
                    performClick(); // Handle click event
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_MOVE:
                    // Handle touch move event
                    return true; // Consume the event
                default:
                    return super.onTouchEvent(event);
            }
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                button.setImageResource(R.drawable.warp_selected);
                this.state = Button.State.PRESSED;
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                button.setImageResource(R.drawable.warp);
                this.state = Button.State.NORMAL;
                performClick(); // Handle click event
                return true;
            } else {
                button.setImageResource(R.drawable.warp);
                this.state = Button.State.NORMAL;
                return false;
            }
        }
    }

    @Override
    public boolean performClick() {
        // Call the superclass method first
        boolean handled = super.performClick();

        // Handle the click event here if needed

        return handled;
    }

    public void setState(Button.State state) {
        this.state = state;
        if (this.state == Button.State.PRESSED) {
            button.setImageResource(R.drawable.warp_selected);
        } else {
            button.setImageResource(R.drawable.warp);
        }
    }

    private void rotateAnimation() {
        RotateAnimation rotateForward = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateForward.setDuration(500);
        rotateForward.setFillAfter(true);

        RotateAnimation rotateBackward = new RotateAnimation(720, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateBackward.setDuration(500);
        rotateBackward.setFillAfter(true);

        rotateForward.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                button.startAnimation(rotateBackward);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });


        button.startAnimation(rotateForward);
    }

}
