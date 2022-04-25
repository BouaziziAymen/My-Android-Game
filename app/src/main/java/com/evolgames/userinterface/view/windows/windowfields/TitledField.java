package com.evolgames.userinterface.view.windows.windowfields;

import android.util.Log;

import com.evolgames.userinterface.view.basics.Element;
import com.evolgames.userinterface.view.basics.Text;
import com.evolgames.userinterface.view.layouts.LinearLayout;

public class TitledField<A extends Element> extends LinearLayout {

    private A mAttachment;

    public A getAttachment() {
        return mAttachment;
    }

    public TitledField(String titleString, A attachment) {
        this(titleString,attachment,10);
    }

    public TitledField(String titleString, A attachment,float margin) {
        super(LinearLayout.Direction.Horizontal, margin);
        this.mAttachment = attachment;
        Text title = new Text(titleString, 2);
        title.setLowerBottomY(attachment.getHeight()/2);

        addToLayout(title);
        addToLayout(attachment);
        float maxY = Math.max(title.getLowerBottomY()+title.getHeight(), attachment.getLowerBottomY()+attachment.getHeight());
        float minY = Math.min(title.getLowerBottomY(), attachment.getLowerBottomY());
        setHeight(maxY- minY);


    }

     TitledField(String titleString, A attachment,float margin, float minX) {
        super(LinearLayout.Direction.Horizontal, margin);
        this.mAttachment = attachment;
        Text title = new Text(titleString, 2);
        title.setLowerBottomY(attachment.getHeight()/2);

        addToLayout(title);
        addToLayout(attachment);
        float maxY = Math.max(title.getLowerBottomY()+title.getHeight(), attachment.getLowerBottomY()+attachment.getHeight());
        float minY = Math.min(title.getLowerBottomY(), attachment.getLowerBottomY());
        setHeight(maxY- minY);
        attachment.setLowerBottomX(minX);

    }


    @Override
    public void setErrorDisplay(ErrorDisplay errorDisplay) {
        getAttachment().setErrorDisplay(errorDisplay);
    }

}