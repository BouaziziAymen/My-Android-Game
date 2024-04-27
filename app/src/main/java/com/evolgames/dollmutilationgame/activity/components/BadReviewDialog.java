package com.evolgames.dollmutilationgame.activity.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.evolgames.gameengine.R;

public class BadReviewDialog extends Dialog {

    public BadReviewDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bad_review_dialog);
    }
}
