package com.next.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.next.customview.R;

/**
 * Created by NeXT on 15-4-14.
 */
public class BorderImageView extends View {

    private static final int IMAGE_SCALE_FITXY = 0;

    private Bitmap mImage;
    private int mTextSize;
    private int mTextColor;
    private String mTitleText;
    private int mImageScale;

    private int mWidth;
    private int mHeight;

    private Rect rect;
    private Paint mPaint;
    private Rect mTextBound;

    public BorderImageView(Context context) {
        this(context, null);
    }

    public BorderImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomImageView,
                defStyleAttr,
                0);

        mImage = BitmapFactory.decodeResource(getResources(),
                a.getResourceId(a.getIndex(R.styleable.CustomImageView_image), 0));
        mTextSize = a.getDimensionPixelSize(
                a.getIndex(R.styleable.CustomImageView_titleTextSize),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                16,
                getResources().getDisplayMetrics()));
        mTextColor = a.getColor(a.getIndex(R.styleable.CustomImageView_titleTextColor), Color.BLACK);
        mTitleText = a.getString(a.getIndex(R.styleable.CustomImageView_titleText));
        mImageScale = a.getInt(a.getIndex(R.styleable.CustomImageView_imageScaleType), 0);
        a.recycle();

        rect = new Rect();
        mPaint = new Paint();
        mTextBound = new Rect();
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBound);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
                int desireByText = getPaddingLeft() + getPaddingRight() + mTextBound.width();
                int desire = Math.max(desireByImg, desireByText);
                mWidth = Math.min(desire, specSize);
                break;
            case MeasureSpec.EXACTLY:  //MATCH_PARENT
                mWidth = specSize;
                break;
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                int desire = getPaddingTop() + getPaddingBottom() +
                        mImage.getHeight() + mTextBound.height();
                mHeight = Math.min(desire, specSize);
                break;
            case MeasureSpec.EXACTLY:
                mHeight = specSize;
                break;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //边框
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        /**
         * 如果设置宽度小于文字所占宽度，超出部分用 ... 代替
         */
        if (mTextBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(
                    mTitleText,
                    paint,
                    (float) mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), paint);
        } else {
            canvas.drawText(
                    mTitleText,
                    mWidth / 2 - mTextBound.width() / 2,
                    mHeight / 2 - getPaddingBottom(),
                    mPaint);
        }

        //回收用掉的块
        rect.bottom = mTextBound.height();

        if (mImageScale == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mImage, null, rect, mPaint);
        } else {
            rect.left = mWidth / 2 - mImage.getWidth() / 2;
            rect.right = mWidth / 2 - mImage.getWidth() / 2;
            rect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
            rect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;

            canvas.drawBitmap(mImage, null, rect, mPaint);
        }

    }
}
