package com.rickgoldman.colorstory;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;

import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jason Shepherd on 3/23/2015.
 */

public class DrawingView extends View {


    private Path drawPath, nextDrawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF660000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void setErase(boolean isErase){
        //private boolean erase=isErase;
        if(isErase)
        //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.setColor("#FFFFFFFF");

        else
            drawPaint.setXfermode(null);
    }

    private void setupDrawing(){


        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setBrushSize(float newSize){
        //update size
        //float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                //newSize, getResources().getDisplayMetrics());
        brushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //Scale and resize special bitmaps for crayon effect
        nextDrawPath = drawPath;

        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
        canvas.drawPath(nextDrawPath, drawPaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Random number generator
        Random rand = new Random();
        int minimum = 3;
        int maximum = 8;
        float randomNum = minimum + rand.nextInt((maximum - minimum) + 1);

        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                nextDrawPath.lineTo(touchX-randomNum, touchY-randomNum);
                nextDrawPath.lineTo(touchX+randomNum, touchY+randomNum);

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawCanvas.drawPath(nextDrawPath, drawPaint);

                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setColor(String newColor){
        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
       // drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        drawPaint.setColor(paintColor);
    }

}
