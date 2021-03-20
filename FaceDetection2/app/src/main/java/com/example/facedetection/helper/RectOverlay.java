package com.example.facedetection.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic{
    private int mRectColor = Color.GREEN;
    private float mStrokeWidth =4.0f;
    private Paint mRectPaint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;

    public RectOverlay(GraphicOverlay overlay, Rect rect) {
        super(overlay);
        mRectPaint = new Paint();
        mRectPaint.setColor(mRectColor);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(mStrokeWidth);

        this.graphicOverlay=graphicOverlay;
        this.rect=rect;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {

        RectF rectf=new RectF(rect);
        rectf.left=translateX(rectf.left);
        rectf.right=translateX(rectf.right);
        rectf.top=translateX(rectf.top);
        rectf.bottom=translateX(rectf.bottom);

        canvas.drawRect(rectf,mRectPaint);

    }
}
