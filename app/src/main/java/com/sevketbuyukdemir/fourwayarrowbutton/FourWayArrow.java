package com.sevketbuyukdemir.fourwayarrowbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class FourWayArrow extends View {
    Context context;
    /*
     * Variables for giving shape to FourWayArrow
     * See : drawOuterCircle(Canvas canvas), drawInnerCircle(Canvas canvas), drawArrows(Canvas canvas)
     */
    private float innerCircleDiameter = 150;
    Rect rectF = new Rect();
    private boolean isArrowUnassigned = false;
    private int topArrowTopPointX;
    private int topArrowTopPointY;
    private int rightArrowRightPointX;
    private int rightArrowRightPointY;
    private int leftArrowLeftPointX;
    private int leftArrowLeftPointY;
    private int bottomArrowBottomPointX;
    private int bottomArrowBottomPointY;

    /*
     * Colors for customize paints
     * See : init(Context context, @Nullable AttributeSet attrs, int defStyle)
     */
    private int outerCircleColor = Color.argb(100, 150, 150, 150);
    private int innerCircleColor = Color.argb(100, 80, 120, 150);
    private int arrowColor = Color.argb(100, 255, 255, 255);

    /*
     * Paints for draw FourWayArrow to screen with Canvas
     * See : onDraw(Canvas canvas)
     */
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private Paint arrowPaint;

    /**
     * Listener for FourWayArrow view which is define from Activity where is use this view.
     */
    private OnFourWayArrowTouchListener onSeekBarChangeListener;

    public static final int UP_BUTTON_PRESSED = 1;
    public static final int RIGHT_BUTTON_PRESSED = 2;
    public static final int LEFT_BUTTON_PRESSED = 3;
    public static final int BOTTOM_BUTTON_PRESSED = 4;

    /**
     * Set function for OnFourWayArrowTouchListener, call from activity after define FourWayArrow view.
     * @param onSeekBarChangeListener
     */
    public void setOnFourWayArrowTouchListener(OnFourWayArrowTouchListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }

    /**
     * Listener interface for define required functions.
     */
    public interface OnFourWayArrowTouchListener {
        public abstract void onStartTrackingTouch(FourWayArrow fourWayArrow, int WHICH_BUTTON);
        public abstract void onMoveTrackingTouch(FourWayArrow fourWayArrow, int WHICH_BUTTON);
        public abstract void onStopTrackingTouch(FourWayArrow fourWayArrow, int WHICH_BUTTON);
    }

    /**
     * Get customization from developer's XML define and set to FourWayArrow view
     * Create Paint's for draw FourWayArrow view
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void init(Context context, @Nullable AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FourWayArrow,
                0,
                0);

        try {
            innerCircleDiameter = typedArray.getFloat(R.styleable.FourWayArrow_innerCircleDiameter, innerCircleDiameter);
            outerCircleColor = typedArray.getColor(R.styleable.FourWayArrow_outerCircleColor, outerCircleColor);
            innerCircleColor = typedArray.getColor(R.styleable.FourWayArrow_innerCircleColor, innerCircleColor);
            arrowColor = typedArray.getColor(R.styleable.FourWayArrow_arrowColor, arrowColor);
        } finally {
            typedArray.recycle();
        }

        outerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerCirclePaint.setStyle(Paint.Style.FILL);
        outerCirclePaint.setColor(outerCircleColor);

        innerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setColor(innerCircleColor);

        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setStrokeWidth(8);
        arrowPaint.setColor(arrowColor);
    }

    public FourWayArrow(Context context) {
        super(context);
        this.context = context;
        init(context, null, 0);

    }

    public FourWayArrow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs, 0);
    }

    public FourWayArrow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
         * Essential to get the perfect apartment. (If the software developer enters wrong values,
         * we ensure that the shape is not distorted.)
         */
        final int intrinsicSize = 200;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(intrinsicSize, widthSize);
        } else {
            width = intrinsicSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(intrinsicSize, heightSize);
        } else {
            height = intrinsicSize;
        }

        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
    }

    private float cx, cy, radius;

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isArrowUnassigned) {
            this.getGlobalVisibleRect(rectF);
            cx = rectF.left + rectF.width()/2.0f;
            cy = rectF.top + rectF.height()/2.0f;
            radius = rectF.width()/2.0f;
            topArrowTopPointX = rectF.left + rectF.width()/2;
            topArrowTopPointY = rectF.top;
            rightArrowRightPointX = rectF.left + rectF.width();
            rightArrowRightPointY = rectF.top + rectF.height()/2;
            leftArrowLeftPointX = rectF.left;
            leftArrowLeftPointY = rectF.top + rectF.height()/2;
            bottomArrowBottomPointX = rectF.left + rectF.width()/2;
            bottomArrowBottomPointY = rectF.top + rectF.height();
            isArrowUnassigned = true;
        }
        super.onDraw(canvas);
        drawOuterCircle(canvas, cx, cy, radius);
        drawInnerCircle(canvas, cx, cy, radius);
        drawArrows(canvas);
    }

    private void drawOuterCircle(Canvas canvas, float cx, float cy, float radius) {
        canvas.drawCircle(cx, cy, radius, outerCirclePaint);
    }

    private void drawInnerCircle(Canvas canvas, float cx, float cy, float radius) {
        canvas.drawCircle(cx, cy, innerCircleDiameter, innerCirclePaint);
    }

    private void drawArrows(Canvas canvas) {
        Path path_top = new Path();
        path_top.moveTo(topArrowTopPointX, topArrowTopPointY);
        path_top.lineTo((topArrowTopPointX + innerCircleDiameter), (topArrowTopPointY + innerCircleDiameter));
        path_top.lineTo((topArrowTopPointX - innerCircleDiameter),  (topArrowTopPointY + innerCircleDiameter));
        path_top.close();
        //path_top.offset(10, 40);
        canvas.drawPath(path_top, arrowPaint);

        Path path_right = new Path();
        path_right.moveTo(rightArrowRightPointX, rightArrowRightPointY);
        path_right.lineTo((rightArrowRightPointX - innerCircleDiameter) , (rightArrowRightPointY - innerCircleDiameter));
        path_right.lineTo((rightArrowRightPointX - innerCircleDiameter) , (rightArrowRightPointY + innerCircleDiameter));
        path_right.close();
        //path_right.offset(10, 40);
        canvas.drawPath(path_right, arrowPaint);

        Path path_left = new Path();
        path_left.moveTo(leftArrowLeftPointX, leftArrowLeftPointY);
        path_left.lineTo((leftArrowLeftPointX + innerCircleDiameter), (leftArrowLeftPointY - innerCircleDiameter));
        path_left.lineTo((leftArrowLeftPointX + innerCircleDiameter), (leftArrowLeftPointY + innerCircleDiameter));
        path_left.close();
        //path_left.offset(10, 40);
        canvas.drawPath(path_left, arrowPaint);

        Path path_bottom = new Path();
        path_bottom.moveTo(bottomArrowBottomPointX, bottomArrowBottomPointY);
        path_bottom.lineTo((bottomArrowBottomPointX + innerCircleDiameter), (bottomArrowBottomPointY - innerCircleDiameter));
        path_bottom.lineTo((bottomArrowBottomPointX - innerCircleDiameter), (bottomArrowBottomPointY - innerCircleDiameter));
        path_bottom.close();
        //path_bottom.offset(10, 40);
        canvas.drawPath(path_bottom, arrowPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float distance = distanceToCenter(event.getX(), event.getY());
        float outerCircleRadius = getOuterCircleRadius();

        if (distance < outerCircleRadius) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onStartTrackingTouch(this, calculatePressedButton(event));
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onMoveTrackingTouch(this, calculatePressedButton(event));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (onSeekBarChangeListener != null) {
                        onSeekBarChangeListener.onStopTrackingTouch(this, calculatePressedButton(event));
                    }
                    break;
            }
        }

        invalidate();
        return true;
    }

    // todo
    private int calculatePressedButton(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(y < (rectF.top + innerCircleDiameter)) {
            return UP_BUTTON_PRESSED;
        } else if(x > (rectF.right - innerCircleDiameter)){
            return RIGHT_BUTTON_PRESSED;
        } else if (y > (rectF.bottom - innerCircleDiameter)){
            return BOTTOM_BUTTON_PRESSED;
        } else if (x < (rectF.left + innerCircleDiameter)) {
            return LEFT_BUTTON_PRESSED;
        } else {
            return -1;
        }
    }

    private float getDiameter() {
        return Math.min(getWidth(), getHeight());
    }

    private float getOuterCircleRadius() {
        return getDiameter() / 2f;
    }

    private PointF getCenter() {
        return new PointF(getWidth() / 2, getHeight() / 2);
    }

    private float distanceToCenter(float x, float y) {
        PointF c = getCenter();
        return (float) Math.sqrt(Math.pow(x - c.x, 2.0) + Math.pow(y - c.y, 2.0));
    }

}
