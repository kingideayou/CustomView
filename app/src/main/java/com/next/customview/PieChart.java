package com.next.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
 * Created by NeXT on 15-4-14.
 */
public class PieChart extends View {

    private int mTextPos;
    private int mTextColor;
    private float mTextHeight;
    private float mTextWidth;
    private boolean mShowText;
    private String mTextString;
    private int mTextSize;

    private Rect mBound;

    private Paint mTextPaint;
    private Paint mPiePaint;
    private Paint mShadowPaint;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PieChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieChart,
                defStyleAttr, 0);

        try {
//            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
            mShowText = true;
            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
            mTextColor = a.getColor(R.styleable.PieChart_textColor, R.color.white);
            mTextHeight = a.getFloat(R.styleable.PieChart_textHeight, 0);
            mTextWidth = a.getFloat(R.styleable.PieChart_textWidth, 0);
            mTextString = a.getString(R.styleable.PieChart_textString);
            // 默认设置为16sp，TypeValue也可以把sp转化为px
            mTextSize = a.getDimensionPixelSize(
                    a.getIndex(R.styleable.PieChart_textSize),
                    (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    16,
                    getResources().getDisplayMetrics()));
        } finally {
            a.recycle();
        }

        initPaint();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextString = randomText();
                initBound();
                postInvalidate();
            }
        });
    }

    private String randomText() {
        Random radom = new Random();
        String[] strs = new String[]{"认真你就赢了","呵呵呵呵呵","你瞅啥","瞅你咋地"};
        int randomInt = radom.nextInt(4);
        return strs[randomInt];
    }

    private void initPaint() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextSize);
        }

        initBound();

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);
        mPiePaint.setTextSize(mTextHeight);
        mPiePaint.setColor(getResources().getColor(android.R.color.holo_blue_bright));

        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xff101010);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

    }

    private void initBound() {
        mBound = new Rect();
        mTextPaint.getTextBounds(mTextString, 0, mTextString.length(), mBound);
    }

    public boolean isShowText() {
        return mShowText;
    }

    public void setShowText(boolean showText) {
        mShowText = showText;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPiePaint);

        canvas.drawText(
                mTextString,
                getWidth() / 2 - mBound.width() / 2,
                getHeight() / 2 + mBound.height() / 2,
                mTextPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY: //MATCH_PARENT
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST: //WRAP_CONTENT
                width = getPaddingLeft() + getPaddingRight() + mBound.width();
                break;
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY: //MATCH_PARENT
                height = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST: //WRAP_CONTENT
                height = getPaddingTop() + getPaddingBottom() + mBound.height();
                break;
        }
        setMeasuredDimension(width, height);
        /*
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) - (int)mTextWidth + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int)mTextWidth, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
        */
    }
}
