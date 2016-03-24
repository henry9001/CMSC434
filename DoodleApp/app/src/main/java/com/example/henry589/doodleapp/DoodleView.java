package com.example.henry589.doodleapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by henry589 on 3/23/2016.
 */
public class DoodleView extends View {


    private Paint doodlePaint = new Paint();
    private Path path = new Path();
    private Bitmap image;
    private Canvas drawCanvas;
    private boolean erase=false;

    private int paintColor = 0xFF660000;

    private float currentBrushSize, lastBrushSize;

    public DoodleView(Context context) {
        super(context);
    }

    public DoodleView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }


    public DoodleView(Context context, AttributeSet attrs, int style){
        super(context, attrs, style);
        init();
    }

    @Override
    public void onDraw(Canvas canvas){

        canvas.drawBitmap(image, 0, 0, doodlePaint);

        canvas.drawPath(path, doodlePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){

        super.onSizeChanged(w,h,oldw,oldh);

        image = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(image);
    }

    private void init(){

        currentBrushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = currentBrushSize;

        doodlePaint.setColor(Color.RED);
        doodlePaint.setAntiAlias(true);
        doodlePaint.setStrokeWidth(currentBrushSize);
        doodlePaint.setStyle(Paint.Style.STROKE);
        doodlePaint.setStrokeJoin(Paint.Join.ROUND);
        doodlePaint.setStrokeCap(Paint.Cap.ROUND);



    }


   public void setPaintColor(String newColor){


        invalidate();
        paintColor = Color.parseColor(newColor);
        doodlePaint.setColor(paintColor);



    }

    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        currentBrushSize=pixelAmount;
        doodlePaint.setStrokeWidth(currentBrushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(path, doodlePaint);
                path.reset();
                break;
            default:
                return false;

        }
        invalidate();
        return true;
    }


    public Bitmap save() {
        return getBitmapFromView();
    }

    private Bitmap getBitmapFromView() {
        final Bitmap returnedBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(returnedBitmap);
        this.draw(canvas);
        return returnedBitmap;
    }

    public void setErase(boolean isErase){
        erase = isErase;

        if(erase) doodlePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else doodlePaint.setXfermode(null);

    }
}
