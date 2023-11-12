package com.evolgames.gameengine;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;

import android.graphics.Bitmap;

public class BitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource 
{
    private final int[] mColors;
 
    public BitmapTextureAtlasSource(Bitmap pBitmap)
    {
    	super(0,0, pBitmap.getWidth(), pBitmap.getHeight());

        this.mColors = new int[this.mTextureWidth * this.mTextureHeight];
        
        for(int y = 0; y < this.mTextureHeight; ++y)
        {
        	for(int x = 0; x < this.mTextureWidth; ++x)
        	{
                this.mColors[x + y * this.mTextureWidth] = pBitmap.getPixel(x, y);
        	}
        }
    }

	@Override
	public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig)
	{
		return Bitmap.createBitmap(this.mColors, this.mTextureWidth, this.mTextureHeight, Bitmap.Config.ARGB_8888);
	}

	@Override
	public IBitmapTextureAtlasSource deepCopy()
	{
		return new BitmapTextureAtlasSource(Bitmap.createBitmap(this.mColors, this.mTextureWidth, this.mTextureHeight, Bitmap.Config.ARGB_8888));
	}

	@Override
	public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig, boolean pMutable) {

		return Bitmap.createBitmap(this.mColors, this.mTextureWidth, this.mTextureHeight, Bitmap.Config.ARGB_8888);
	}
}
