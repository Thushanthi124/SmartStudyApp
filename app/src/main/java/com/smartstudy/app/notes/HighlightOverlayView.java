package com.smartstudy.app.notes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Matrix;
import android.util.Log;


public class HighlightOverlayView extends View {


    private boolean highlightModeEnabled = false;

    public void setHighlightMode(boolean enabled) {
        highlightModeEnabled = enabled;
    }

    public interface OnNewHighlightListener {
        void onNewHighlight(RectF normalizedRect);
    }

    private final Matrix imageMatrix = new Matrix();

    public void setImageMatrix(Matrix matrix) {
        if (matrix != null) {
            imageMatrix.set(matrix);
            invalidate();
        }
    }



    private OnNewHighlightListener listener;

    private final Paint paint = new Paint();
    private final List<RectF> highlights = new ArrayList<>();

    private float startX, startY, endX, endY;
    private boolean drawing = false;

    public HighlightOverlayView(Context context) {
        super(context);
        init();
    }

    public HighlightOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(0x55FFEB3B);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setOnNewHighlightListener(OnNewHighlightListener listener) {
        this.listener = listener;
    }

    public void setHighlights(List<RectF> normalizedRects) {
        highlights.clear();
        if (normalizedRects != null) {
            highlights.addAll(normalizedRects);
        }
        invalidate();
    }

    public void clear() {
        highlights.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (highlights.isEmpty() && !drawing) return;

        float[] values = new float[9];
        imageMatrix.getValues(values);

        float scaleX = values[Matrix.MSCALE_X];
        float scaleY = values[Matrix.MSCALE_Y];
        float transX = values[Matrix.MTRANS_X];
        float transY = values[Matrix.MTRANS_Y];

        int w = getWidth();
        int h = getHeight();

        // =============================
        // Draw SAVED highlights
        // =============================
        for (RectF n : highlights) {

            RectF screen = new RectF(
                    n.left * w * scaleX + transX,
                    n.top * h * scaleY + transY,
                    n.right * w * scaleX + transX,
                    n.bottom * h * scaleY + transY
            );

            canvas.drawRect(screen, paint);
        }

        // =============================
        // Draw CURRENT dragging highlight
        // =============================
        if (drawing) {
            RectF drag = new RectF(
                    Math.min(startX, endX),
                    Math.min(startY, endY),
                    Math.max(startX, endX),
                    Math.max(startY, endY)
            );
            canvas.drawRect(drag, paint);
        }
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("HIGHLIGHT_TOUCH", "Touch received: " + event.getAction());

        // 🔥 KEY FIX: if highlight mode is OFF, DO NOT consume touch
        if (!highlightModeEnabled) {
            return false; // let PhotoView handle zoom & pan
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                drawing = true;
                startX = event.getX();
                startY = event.getY();
                endX = startX;
                endY = startY;
                drawing = true;
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                if (!drawing) return false;
                endX = event.getX();
                endY = event.getY();
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                drawing = false;
                endX = event.getX();
                endY = event.getY();

                if (listener != null && getWidth() > 0 && getHeight() > 0) {
                    RectF normalized = new RectF(
                            Math.min(startX, endX) / getWidth(),
                            Math.min(startY, endY) / getHeight(),
                            Math.max(startX, endX) / getWidth(),
                            Math.max(startY, endY) / getHeight()
                    );

                    // ✅ ADD IT LOCALLY FIRST (THIS IS THE FIX)
                    highlights.add(normalized);

                    listener.onNewHighlight(normalized);
                }

                invalidate();
                return true;
        }

        return false;
    }



}
