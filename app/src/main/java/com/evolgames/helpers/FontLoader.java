package com.evolgames.helpers;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.SparseArray;

import com.evolgames.activity.BitmapTextureAtlasSource;

import org.andengine.opengl.font.exception.FontException;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;

public class FontLoader {
    // ===========================================================
    // PhysicsConstants
    // ===========================================================
    private static final String LOADING_LETTERS_LIST =
            "01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,_!?+-#:èé";
    // ===========================================================
    // Fields
    // ===========================================================
    private final Canvas mCanvas = new Canvas();
    private final Paint mPaint;
    private final Paint mBackgroundPaint;
    private final Rect rect = new Rect();
    private final float[] mWidthContainer = new float[1];
    private final BuildableBitmapTextureAtlas mTexture;
    private final SparseArray<ArrayList<MyLetter>> mLoadedFonts = new SparseArray<>();
    private final SparseArray<Font> mFonts = new SparseArray<>();

    public FontLoader(BuildableBitmapTextureAtlas pTexture) {
        this.mTexture = pTexture;
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setColor(Color.TRANSPARENT_ABGR_PACKED_INT);
        this.mBackgroundPaint.setStyle(Style.FILL);
        this.mPaint = new Paint();
    }

    // ===========================================================
    // Constructors
    // ===========================================================

    public void loadFont(
            int pFontId,
            final Typeface pTypeface,
            final float pSize,
            final int pColorARGBPackedInt,
            final boolean pAntiAlias) {
        this.mPaint.setTypeface(pTypeface);
        this.mPaint.setColor(pColorARGBPackedInt);
        this.mPaint.setTextSize(pSize);
        this.mPaint.setAntiAlias(pAntiAlias);
        loadLetters(pFontId);
        mFonts.append(pFontId, new Font(pTypeface, pSize));
    }

    private void loadLetters(int pFontId) {

        ArrayList<MyLetter> letters = new ArrayList<>();
        letters.add(createLetter(' '));
        for (int i = 0; i < FontLoader.LOADING_LETTERS_LIST.length(); i++) {
            char character = FontLoader.LOADING_LETTERS_LIST.charAt(i);

            MyLetter myLetter = createLetter(character);
            letters.add(myLetter);
        }
        mLoadedFonts.put(pFontId, letters);
    }

    private float getLetterAdvance(final String pCharacterAsString) {
        this.mPaint.getTextWidths(pCharacterAsString, this.mWidthContainer);
        return this.mWidthContainer[0];
    }

    private MyLetter createLetter(char character) throws FontException {

        final String characterAsString = String.valueOf(character);
        mPaint.getTextBounds(new char[]{character}, 0, 1, rect);
        int width = rect.width();
        int height = rect.height();
        int offsetX = 0;
        int offsetY = rect.bottom;

        float advance = getLetterAdvance(characterAsString);
        if (Character.isWhitespace(character)) return new MyLetter(advance);

        final Bitmap letterBitmap = Bitmap.createBitmap(width + 2, height + 2, Config.ARGB_8888);
        this.mCanvas.setBitmap(letterBitmap);
        /* Make background transparent. */
        this.mCanvas.drawRect(
                0, 0, letterBitmap.getWidth(), letterBitmap.getHeight(), this.mBackgroundPaint);

        /* Actually draw the character. */
        this.drawLetter(characterAsString, 1, 1 - rect.top);

        TextureRegion letterTextureRegion =
                BitmapTextureAtlasTextureRegionFactory.createFromSource(
                        mTexture, new BitmapTextureAtlasSource(letterBitmap));

        return new MyLetter(advance, letterTextureRegion, offsetX, offsetY);
    }

    private void drawLetter(final String pCharacterAsString, final float pLeft, final float pTop) {
        this.mCanvas.drawText(pCharacterAsString, pLeft, pTop, this.mPaint);
    }

    public MyLetter getLetter(int fontId, char character) {
        int index =
                (Character.isWhitespace(character)) ? 0 : LOADING_LETTERS_LIST.indexOf(character) + 1;
        return mLoadedFonts.get(fontId).get(index);
    }

    public float getHeight(int fontId) {
        Font font = mFonts.get(fontId);
        mPaint.setTypeface(font.typeFace);
        mPaint.setTextSize(font.size);
        mPaint.getTextBounds(LOADING_LETTERS_LIST, 0, LOADING_LETTERS_LIST.length(), rect);
        return rect.height();
    }

    public float getWidth(int fontId, String text) {
        Font font = mFonts.get(fontId);
        mPaint.setTypeface(font.typeFace);
        mPaint.setTextSize(font.size);
        mPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    class Font {
        Typeface typeFace;
        float size;

        public Font(Typeface typeFace, float size) {
            this.typeFace = typeFace;
            this.size = size;
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
