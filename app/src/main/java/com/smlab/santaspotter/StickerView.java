package com.smlab.santaspotter;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.smlab.santaspotter.filter.ColorFilterGenerator;

public class StickerView extends View {

    private Bitmap backgroundImage;
    private PointF pivotPoint = new PointF();
    private Bitmap sticker;
    private boolean isStickerFlipped = false; // Flag to track the flip state
    private Bitmap removeIcon; // Cross icon for sticker removal
    private Matrix removeIconMatrix;
//    private int stickerBrightness = 128;

    //    Nov 29, 2023  -   Give the value 55 so make sure sticker origin color not changed
    private int stickerBrightness = 55;
    private Matrix stickerMatrix;
    private Paint paint;
    private float oldDist;
    private static final int INVALID_POINTER_ID = -1;
    private int activePointerId = INVALID_POINTER_ID;

    private float lastTouchX;
    private float lastTouchY;
    private static final int CANCEL_BUTTON_SIZE = 40;
    float stickerTemperature = 20;
    private float lastRotation = 180f;

    private OnStickerRemoveListener stickerRemoveListener;


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

    private OnStickerTouchListener stickerTouchListener;

    public interface OnStickerTouchListener {
        void onStickerTouched();
    }

    public void setStickerTouchListener(OnStickerTouchListener listener) {
        this.stickerTouchListener = listener;
    }


    public void setBackgroundImage(Bitmap background) {
        this.backgroundImage = background;
        invalidate();
    }

    public void addSticker(Bitmap sticker) {
        this.sticker = sticker;
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

    // Add a method to set brightness
    public void setStickerBrightness(int brightness) {
        isBrightnessMode = true;
        if (brightness != stickerBrightness) {
            this.stickerBrightness = brightness;
//            invalidate();
            postInvalidate();
        }
    }

    public void setStickerTemperature(int temperature) {
        isBrightnessMode = false;
        if (temperature != stickerTemperature) {
            this.stickerTemperature = temperature;
//            invalidate();
            postInvalidate();
        }
    }

    private boolean isBrightnessMode = true;

    @SuppressLint("DrawAllocation")
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

        if (sticker != null) {
            // Apply brightness filter
            if (isBrightnessMode) {
                Paint brightnessPaint = new Paint(paint);
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.set(new float[]{
                        1, 0, 0, 0, stickerBrightness - 55,
                        0, 1, 0, 0, stickerBrightness - 55,
                        0, 0, 1, 0, stickerBrightness - 55,
                        0, 0, 0, 1, 0
                });

                Log.d(TAG, "onDraw: brightness: " + stickerBrightness);
                brightnessPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

//            Nov 29,2023   -   brightness paint ==null because the default sticker is by default bright.

                canvas.drawBitmap(sticker, stickerMatrix, brightnessPaint);
//            canvas.drawBitmap(sticker, stickerMatrix, null);
            } else {
                Paint temperaturePaint = new Paint(paint);
                ColorMatrix colorMatrix1 = new ColorMatrix();

                float rScale = 1.0f + (stickerTemperature / 100.0f);  // Adjust the temperature factor
                Log.d(TAG, "onDraw: Temperature: " + rScale);
                Log.d(TAG, "onDraw: Temperature:stickerTemperature: " + stickerTemperature);
                colorMatrix1.set(new float[]{
                        rScale, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0
                });

                temperaturePaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix1));

                canvas.drawBitmap(sticker, stickerMatrix, temperaturePaint);
            }

        }


    }


    private void drawCancelButton(Canvas canvas) {
        if (sticker != null) {
            float[] points = getStickerTopRight(); // Use the top-right corner of the sticker
            float x = points[0];
            float y = points[1];

            // Draw cancel button in white color
            Paint cancelButtonPaint = new Paint();
            cancelButtonPaint.setColor(Color.RED);
            cancelButtonPaint.setStyle(Paint.Style.FILL);

            // Reduce the size of the cancel button
            float cancelButtonSize = CANCEL_BUTTON_SIZE / 2;

            // Adjust the position of the cancel button to make it closer to the sticker
            float adjustedX = x - cancelButtonSize * 2.5f;
            float adjustedY = y + cancelButtonSize * 1.5f;

            // Draw cancel button as a white circle
            canvas.drawCircle(adjustedX, adjustedY, cancelButtonSize, cancelButtonPaint);

            // Reduce the size of the cross icon
            float crossSize = cancelButtonSize * 0.4f;

            // Draw a black cross inside the white circle
            Paint crossPaint = new Paint();
            crossPaint.setColor(Color.WHITE);
            crossPaint.setStrokeWidth(3); // Adjust the stroke width as needed

            // Calculate cross coordinates
            float startX = adjustedX - crossSize;
            float startY = adjustedY - crossSize;
            float endX = adjustedX + crossSize;
            float endY = adjustedY + crossSize;

            // Draw the cross
            canvas.drawLine(startX, startY, endX, endY, crossPaint);
            canvas.drawLine(startX, endY, endX, startY, crossPaint);
        }
    }
    private float[] getStickerTopRight() {
        float[] points = {sticker.getWidth(), 0};
        stickerMatrix.mapPoints(points);
        return points;
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
                lastRotation = getRotation(event);
                break;

            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    float newDist = getDistance(event);
                    float scale = newDist / oldDist;

                    // Scale
                    stickerMatrix.postScale(scale, scale, lastTouchX, lastTouchY);

                    // Rotation
                    float newRotation = getRotation(event);
                    float deltaRotation = newRotation - lastRotation;
                    stickerMatrix.postRotate(deltaRotation, lastTouchX, lastTouchY);

                    oldDist = newDist;
                    lastRotation = newRotation;
                } else if (activePointerId != INVALID_POINTER_ID) {
                    float deltaX = x - lastTouchX;
                    float deltaY = y - lastTouchY;

                    // Translation
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

                if (isTouchOnSticker(x, y) && stickerTouchListener != null) {
                    stickerTouchListener.onStickerTouched();
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


    private boolean isTouchOnSticker(float x, float y) {
        if (sticker != null) {
            float[] points = getStickerTopLeft(); // Use the top-left corner of the sticker
            float stickerX = points[0];
            float stickerY = points[1];

            // Check if the touch coordinates are within the sticker area
            return x >= stickerX && x <= (stickerX + sticker.getWidth()) &&
                    y >= stickerY && y <= (stickerY + sticker.getHeight());
        }
        return false;
    }

    private boolean isTouchInsideCancelButton(float x, float y) {
        if (sticker != null) {
            float[] points = getStickerTopRight(); // Use the top-right corner of the sticker
            float stickerX = points[0];
            float stickerY = points[1];

            // Check if the touch coordinates are within the cancel button area
            return x >= (stickerX - CANCEL_BUTTON_SIZE) && x <= (stickerX + CANCEL_BUTTON_SIZE) &&
                    y >= (stickerY - CANCEL_BUTTON_SIZE) && y <= (stickerY + CANCEL_BUTTON_SIZE);
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



    public void flipSticker() {
        if (sticker != null) {
            // Get the current center coordinates of the sticker
            float[] stickerCenter = getStickerCenter();

            // Flip the sticker horizontally using a matrix
            Matrix flipMatrix = new Matrix();
            flipMatrix.postScale(-1, 1, stickerCenter[0], stickerCenter[1]);
            stickerMatrix.postConcat(flipMatrix);

            // Toggle the flip state
            isStickerFlipped = !isStickerFlipped;

            invalidate();
        }
    }

    public Bitmap createBitmap() {
        // Create a bitmap with the same size as the view
        Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw the view onto the canvas
        draw(canvas);

        return bitmap;
    }

    private float[] getStickerCenter() {
        float[] points = {sticker.getWidth() / 2f, sticker.getHeight() / 2f};
        stickerMatrix.mapPoints(points);
        return points;
    }

    private float getRotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public interface OnStickerRemoveListener {
        void onStickerRemoved();
    }
    public void setStickerRemoveListener(OnStickerRemoveListener listener) {
        this.stickerRemoveListener = listener;
    }

    public void removeSticker() {
        if (stickerRemoveListener != null) {
            stickerRemoveListener.onStickerRemoved();
        }
        clearSticker();
        invalidate();
    }




}