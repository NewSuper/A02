package com.xxx.demo.keep;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FunnyCanvasUtils {
    public static void drawCenterText(Canvas canvas, String text, float x, float y, Rect rect, Paint paint){
        paint.getTextBounds(text,0,text.length(),rect);
        Paint.FontMetrics fontMetrics=paint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=y+distance;
        canvas.drawText(text,x-rect.width()/2f,baseline,paint);
    }

    public static void drawCenterText(Canvas canvas,int text,float x,float y,Rect rect,Paint paint){
        drawCenterText(canvas,""+text,x,y,rect,paint);
    }
}
