package com.ptmlb.ca.ahgroup.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * Created by fmisser on 2016/6/28.
 *
 */

public class RotateLogoDrawable extends Drawable {

    private Paint paint;
    private Bitmap bitmap;
    private float angle;
    private float scale;
    private int width, height;
    private Point centrePoint;

    public RotateLogoDrawable(Bitmap bitmap) {
        this.paint = new Paint();
        this.bitmap = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setCentrePoint(Point centrePoint) {
        this.centrePoint = centrePoint;
    }

    public Point getCentrePoint() {
        return this.centrePoint;
    }

    @Override
    public int getIntrinsicWidth() {
        return this.width;
    }

    @Override
    public int getIntrinsicHeight() {
        return this.height;
    }

    @Override
    public void draw(Canvas canvas) {

        if (angle > 0.0f) {
            angle = 0.0f;
        } else if (angle < -180.0f) {
            angle = -180.0f;
        }

        if (scale < 0.5f) {
            scale = 0.5f;
        } else if (scale > 1.0f){
            scale = 1.0f;
        }

        Matrix matrix = new Matrix();
        matrix.postTranslate(centrePoint.x-width/2, centrePoint.y-height/2);
        matrix.postRotate(angle, centrePoint.x, centrePoint.y);
        matrix.postScale(scale, scale, centrePoint.x, centrePoint.y/2);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
