package com.evolgames.activity.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.evolgames.gameengine.R;
import com.evolgames.userinterface.view.inputs.Button;


public class OptionButton extends FrameLayout {
    private ImageView button;
    private ImageView icon;
    private Button.State state;
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

    public void setIcon(int resourceId) {
        icon.setImageResource(resourceId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (state == Button.State.NORMAL){
                        button.setImageResource(R.drawable.warp_selected);
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
        if(this.state == Button.State.PRESSED){
            button.setImageResource(R.drawable.warp_selected);
        } else {
            button.setImageResource(R.drawable.warp);
        }
    }

}
