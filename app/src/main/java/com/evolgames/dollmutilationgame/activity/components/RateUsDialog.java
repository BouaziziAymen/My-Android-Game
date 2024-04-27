package com.evolgames.dollmutilationgame.activity.components;

import android.app.Dialog;
import android.os.Bundle;

import com.evolgames.dollmutilationgame.activity.GameActivity;
import com.evolgames.gameengine.R;

public class RateUsDialog extends Dialog {

    private final GameActivity gameActivity;

    public RateUsDialog(GameActivity gameActivity) {
        super(gameActivity);
        this.gameActivity = gameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us_dialog);

        GameImageButton fiveStarsButton = findViewById(R.id.five_stars_button);
        GameImageButton badReviewButton = findViewById(R.id.bad_review_button);
        fiveStarsButton.setOnPressed(()->{
            gameActivity.openPlayStoreForReview();
            dismiss();
        });
        badReviewButton.setOnPressed(()->{
            gameActivity.openBadReviewDialog();
                dismiss();
        });

    }

}

