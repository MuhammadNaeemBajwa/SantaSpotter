package com.smlab.santaspotter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class StickerView extends View {

    private Bitmap backgroundImage;
    private ObjectAnimator rotationAnimator;
    private float rotationDegrees = 180;

    private Bitmap sticker;
    private Bitmap removeIcon; // Cross icon for sticker removal
    private Matrix removeIconMatrix;
    private Matrix stickerMatrix;
    private Paint paint;
    private float oldDist;
    private static final int INVALID_POINTER_ID = -1;
    private int activePointerId = INVALID_POINTER_ID;

    private float lastTouchX;
    private float lastTouchY;
    private static final int CANCEL_BUTTON_SIZE = 100;

    public StickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        stickerMatrix = new Matrix();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
    }

    public void setBackgroundImage(Bitmap background) {
        this.backgroundImage = background;
        invalidate();
    }

    public void addSticker(Drawable stickerDrawable) {
        this.sticker = drawableToBitmap(stickerDrawable);
        if (sticker != null) {
            Log.d("StickerView", "Sticker added successfully");
            invalidate();
        } else {
            Log.e("StickerView", "Failed to convert drawable to bitmap");
        }
    }


    public void clearSticker() {
        this.sticker = null;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (backgroundImage != null) {
            canvas.drawBitmap(backgroundImage, 0, 0, paint);
        }

        if (sticker != null) {
            canvas.drawBitmap(sticker, stickerMatrix, paint);

            // Draw remove icon at the top-left corner of the sticker
            if (removeIcon != null) {
                float[] points = getStickerTopLeft();
                removeIconMatrix.reset();
                removeIconMatrix.postTranslate(points[0], points[1]);
                canvas.drawBitmap(removeIcon, removeIconMatrix, paint);
            }

            // Draw cancel button on the sticker
            drawCancelButton(canvas);
        }

    }
    private void drawCancelButton(Canvas canvas) {
        if (sticker != null) {
            float[] points = getStickerTopLeft();
            float x = points[0];
            float y = points[1];


            // Draw cancel button in white color
            Paint cancelButtonPaint = new Paint();
//            cancelButtonPaint.setColor(Color.WHITE);
//            cancelButtonPaint.setColor(0xFFFFFFFF);// Set the color to white
            cancelButtonPaint.setColor(Color.WHITE);// Set the color to white
//            cancelButtonPaint.setStyle(Paint.Style.FILL);
            // Draw cancel button
            // Modify the code below based on the appearance you want for the cancel button
            canvas.drawCircle(x, y, CANCEL_BUTTON_SIZE / 2, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                activePointerId = event.getPointerId(0);
                lastTouchX = x;
                lastTouchY = y;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = getDistance(event);
                break;

            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    float newDist = getDistance(event);
                    float scale = newDist / oldDist;
                    stickerMatrix.postScale(scale, scale, lastTouchX, lastTouchY);
                    oldDist = newDist;

                    float angle = getRotation(event);
                    rotationDegrees += angle;

                    // Smooth rotation animation
                    if (rotationAnimator != null) {
                        rotationAnimator.cancel();
                    }

                    rotationAnimator = ObjectAnimator.ofFloat(this, "rotationDegrees", rotationDegrees);
                    rotationAnimator.setDuration(100); // Adjust the duration as needed
                    rotationAnimator.start();

                    float deltaX = x - lastTouchX;
                    float deltaY = y - lastTouchY;
                    stickerMatrix.postTranslate(deltaX, deltaY);

                } else if (activePointerId != INVALID_POINTER_ID) {
                    float deltaX = x - lastTouchX;
                    float deltaY = y - lastTouchY;
                    stickerMatrix.postTranslate(deltaX, deltaY);
                }

                lastTouchX = x;
                lastTouchY = y;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:

                // Check if the touch event is within the cancel button area
                if (isTouchInsideCancelButton(x, y)) {
                    removeSticker();
                }
                activePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_CANCEL:
                activePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                int pointerIndex = event.getActionIndex();
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == activePointerId) {
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = event.getX(newPointerIndex);
                    lastTouchY = event.getY(newPointerIndex);
                    activePointerId = event.getPointerId(newPointerIndex);
                }
                break;
        }

        return true;
    }

    private boolean isTouchInsideCancelButton(float x, float y) {
        if (sticker != null) {
            float[] points = getStickerTopLeft();
            float stickerX = points[0];
            float stickerY = points[1];

            // Check if the touch coordinates are within the cancel button area
            return x >= stickerX && x <= (stickerX + CANCEL_BUTTON_SIZE) &&
                    y >= stickerY && y <= (stickerY + CANCEL_BUTTON_SIZE);
        }
        return false;
    }


    private float[] getStickerTopLeft() {
        float[] points = {0, 0};
        stickerMatrix.mapPoints(points);
        return points;
    }

    private float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    public void removeSticker() {
        clearSticker(); // This method clears the sticker
        invalidate();
    }

    private float getRotation(MotionEvent event) {
        double deltaX = event.getX(0) - event.getX(1);
        double deltaY = event.getY(0) - event.getY(1);
        double radians = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radians);
    }
    public float getRotationDegrees() {
        return rotationDegrees;
    }

    public void setRotationDegrees(float degrees) {
        this.rotationDegrees = degrees;
        invalidate();
    }
}