package com.evolgames.userinterface.view.windows.windowfields;

import android.util.Log;

import com.evolgames.userinterface.view.Color;
import com.evolgames.userinterface.view.Colors;
import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.layouts.LinearLayout;

public class FieldWithError extends LinearLayout implements ErrorDisplay {
    private final Text errorText;
    boolean showError = false;
    Element errorProducer;

    public FieldWithError(Element errorProducer) {
        super(Direction.Vertical);
        this.errorProducer = errorProducer;
        addToLayout(this.errorProducer);
        errorText = new Text("", 3);
        addToLayout(errorText);
        Color color = Colors.palette1_red;
        errorText.setColor(color.getRed(), color.getGreen(), color.getBlue());
        this.errorProducer.setErrorDisplay(this);
        setLowerBottomY(getHeight());
    }

    @Override
    public float getHeight() {
        if(showError)return errorText.getHeight() + errorProducer.getHeight()+5;
        else return errorProducer.getHeight();
    }

    @Override
    public void showError(String error) {
        showError = true;
        errorText.setVisible(true);
        if(error!=null)
        errorText.updateText(error);
        setLowerBottomY(getHeight());
    }


    @Override
    public void hideError() {
        showError = false;
        errorText.setVisible(false);
        setLowerBottomY(getHeight());
    }
}
