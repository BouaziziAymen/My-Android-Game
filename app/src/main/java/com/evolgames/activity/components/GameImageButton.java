package com.evolgames.activity.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.evolgames.gameengine.R;
import com.evolgames.userinterface.view.inputs.Button;


public class GameImageButton extends FrameLayout {
    private ImageView button;
    private ImageView icon;
    private ButtonType buttonType;
    private Button.State state;

    private Runnable onPressed;
    private Runnable onReleased;

    public enum ButtonType {
        SELECT,CLICK
    }

    public GameImageButton(Context context) {
        super(context);
        init();
    }

    public GameImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.image_button, this, true);
        button = findViewById(R.id.image);
        icon = findViewById(R.id.icon);
    }
    // Method to set the image resource from outside
    public void setImageResource(int resourceId) {
        icon.setImageResource(resourceId);
    }

    public void setButtonType(GameImageButton.ButtonType buttonType) {
        this.buttonType = buttonType;
    }

    public void setOnPressed(Runnable onPressed) {
        this.onPressed = onPressed;
    }

    public void setOnReleased(Runnable onReleased) {
        this.onReleased = onReleased;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(buttonType== GameImageButton.ButtonType.CLICK) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    button.setImageResource(R.drawable.button_clicked_two);
                    performClick(); // Handle click event
                    return true;
                case MotionEvent.ACTION_UP:
                    // Handle touch up event
                    button.setImageResource(R.drawable.button_released);
                    performClick(); // Handle click event
                    return true; // Consume the event
                case MotionEvent.ACTION_MOVE:
                    // Handle touch move event
                    return true; // Consume the event
                default:
                    return super.onTouchEvent(event);
            }
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(state== Button.State.PRESSED) {
                        button.setImageResource(R.drawable.button_released);
                        this.state = Button.State.NORMAL;
                        if(onReleased !=null){
                            onReleased.run();
                        }
                    } else {
                        button.setImageResource(R.drawable.button_clicked_two);
                        this.state = Button.State.PRESSED;
                        if(onPressed !=null){
                            onPressed.run();
                        }
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
    }

    @Override
    public boolean performClick() {
        // Call the superclass method first
        boolean handled = super.performClick();

        // Handle the click event here if needed

        return handled;
    }
}
