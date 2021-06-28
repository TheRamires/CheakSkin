package ru.skinallergic.checkskin.components.fooddiary;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDrawable extends Drawable {
    private Drawable drawable;
    private String text;

    public MyDrawable(Drawable drawable, String text){
        this.drawable=drawable;
        this.text=text;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        Bitmap mBitmap = ((BitmapDrawable)drawable).getBitmap();
        Bitmap bitmap2=Bitmap.createScaledBitmap(mBitmap, bounds.width(), bounds.height(), false);

        Paint paintText = new Paint();
        paintText.setTextSize((float) (bounds.height()/2.8));
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setAntiAlias(true);
        paintText.setColor(Color.BLACK);
        //paintText.setStrokeWidth(2.0f);
        paintText.setStyle(Paint.Style.FILL);
        //shadowPaint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);

        canvas.drawText(text, (bounds.centerX()), (bounds.bottom+25), paintText);
        canvas.drawBitmap(bitmap2, bounds.left , bounds.top-13,null);

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
