package com.next.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.next.customview.R;

/**
 * Created by NeXT on 15-4-15.
 */
public class CustomProgressBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;

    private Paint mPaint;
    private int mProgress;
    private int mSpeed;

    private boolean isNext = false;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomProgressBar,
                defStyleAttr,
                0
        );

        mFirstColor = a.getColor(a.getIndex(R.styleable.CustomProgressBar_firstColor), Color.BLUE);
        mSecondColor = a.getColor(a.getIndex(R.styleable.CustomProgressBar_secondColor), Color.RED);
        mCircleWidth = a.getDimensionPixelSize(a.getIndex(R.styleable.CustomProgressBar_circleWidth),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
        mSpeed = a.getInt(a.getIndex(R.styleable.CustomProgressBar_speed), 20);

        a.recycle();

        // init Paint
        mPaint = new Paint();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    mProgress = mProgress + 6;
                    if (mProgress == 360) {
                        mProgress = 0;
                        isNext = !isNext;
                    }
                    postInvalidate();
                    try{
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;    //圆心 x 坐标
        int radius = center - mCircleWidth / 2; //半径
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setAntiAlias(true);  //消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);    //空心
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);

        if (!isNext) {
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(center, center, radius, mPaint);  //画圆环
            mPaint.setColor(mSecondColor);
            canvas.drawArc(oval, -90, mProgress, false, mPaint);    //根据进度画圆弧

        } else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mFirstColor);
            canvas.drawArc(oval, -90, mProgress, false, mPaint);
        }

    }
}
