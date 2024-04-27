package com.evolgames.dollmutilationgame.activity.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.userinterface.view.inputs.Button;
import com.evolgames.gameengine.R;


public class GameImageButton extends FrameLayout {
    private ImageView button;
    private ImageView icon;
    private Button.State state = Button.State.NORMAL;

    private Runnable onPressed;
    private Runnable onReleased;

    private ButtonType buttonType;

    public GameImageButton(Context context) {
        super(context);
        init();
    }

    public GameImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GameImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GameImageButton);
            int buttonTypeOrdinal = a.getInt(R.styleable.GameImageButton_buttonType, 0);
            buttonType = ButtonType.values()[buttonTypeOrdinal];
            int iconResId = a.getResourceId(R.styleable.GameImageButton_icon, 0);
            setIcon(iconResId);
            a.recycle();
        }
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.image_button, this, true);
        button = findViewById(R.id.image);
        icon = findViewById(R.id.icon);
    }

    public void setIcon(int resourceId) {
        icon.setImageResource(resourceId);
    }

    public void setOnPressed(Runnable onPressed) {
        this.onPressed = onPressed;
    }

    public void setOnReleased(Runnable onReleased) {
        this.onReleased = onReleased;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (buttonType == GameImageButton.ButtonType.CLICK) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    button.setImageResource(R.drawable.button_clicked_two);
                    if (onPressed != null) {
                        onPressed.run();
                    }
                    ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().onSound,1f,4);
                    performClick(); // Handle click event
                    return true;
                case MotionEvent.ACTION_UP:
                    // Handle touch up event
                    button.setImageResource(R.drawable.button_released);
                    if (onReleased != null) {
                        onReleased.run();
                    }
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
                    if (state == Button.State.PRESSED) {
                        button.setImageResource(R.drawable.button_released);
                        this.state = Button.State.NORMAL;
                        ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().offSound,1f,4);
                        if (onReleased != null) {
                            onReleased.run();
                        }
                    } else if (state == Button.State.NORMAL)  {
                        button.setImageResource(R.drawable.button_clicked_two);
                        this.state = Button.State.PRESSED;
                        ResourceManager.getInstance().tryPlaySound(ResourceManager.getInstance().onSound,1f,4);
                        if (onPressed != null) {
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

    public void setState(Button.State state) {
        this.state = state;
        if (this.state == Button.State.PRESSED) {
            button.setImageResource(R.drawable.button_clicked_two);
        } else {
            button.setImageResource(R.drawable.button_released);
        }
    }

    public enum ButtonType {
        CLICK,
        SELECT
    }
}
