package com.smlab.santaspotter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class PaintView extends View implements View.OnTouchListener {

    private static final String TAG = "PaintView";
    Bitmap bitmap;
    Bitmap transparent;
    int X = -100;
    int Y = -100;
    Canvas canvas;

    Paint paint;

    Path drawPath;


    RectF rectF;


    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        setLayoutParams(params);
    }

    public void addSticker(Context context, Bitmap sticker) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        drawPath = new Path();
        paint = new Paint();

        transparent = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        bitmap = sticker;
        canvas = new Canvas();
        canvas.setBitmap(transparent);
        rectF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bitmap, null, rectF, paint);


        paint.setAlpha(0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(40);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getResultingBitmap() {
        // Create a new Bitmap to hold the result
        Bitmap resultBitmap = Bitmap.createBitmap(transparent.getWidth(), transparent.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a Canvas for the result Bitmap
        Canvas resultCanvas = new Canvas(resultBitmap);

        // Draw the transparent Bitmap onto the result Bitmap
        resultCanvas.drawBitmap(transparent, 0, 0, null);

        // Draw the current path onto the result Bitmap
        resultCanvas.drawPath(drawPath, paint);

        return resultBitmap;
    }


    public void resetView() {
        drawPath.reset();
        bitmap = null;
        transparent = null;
        invalidate();
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void setBrushSize(float size) {
        paint.setStrokeWidth(size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(transparent, null, rectF, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        X = (int) event.getX();
        Y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(X, Y);
                canvas.drawPath(drawPath, paint);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(X, Y);
                canvas.drawPath(drawPath, paint);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(X, Y);
                canvas.drawPath(drawPath, paint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }
}
