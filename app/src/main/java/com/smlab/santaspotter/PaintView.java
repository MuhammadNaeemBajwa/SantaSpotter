package com.smlab.santaspotter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class PaintView extends View implements View.OnTouchListener {

    private static final String TAG = "PaintView";
    Bitmap Bitmap2;
    Bitmap Transparent;
    int X = -100;
    int Y = -100;
    Canvas c2;

    Paint paint = new Paint();

    Path drawPath = new Path();

    public PaintView(Context context, AttributeSet attrs) {
        super(context,attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        setLayoutParams(params);
    }

    public void addSticker(Context context, Bitmap sticker){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Transparent = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.santa);
        Bitmap2 = sticker;

        c2 = new Canvas();
        c2.setBitmap(Transparent);
        c2.drawBitmap(Bitmap2, 0, 0, paint);

        paint.setAlpha(0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
    }

    public void setBrushSize(float size) {
        paint.setStrokeWidth(size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(Transparent, 0, 0, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        X = (int) event.getX();
        Y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(X, Y);
                c2.drawPath(drawPath, paint);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(X, Y);
                c2.drawPath(drawPath, paint);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(X, Y);
                c2.drawPath(drawPath, paint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }
}
