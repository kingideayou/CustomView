package com.next.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class CustomTitleView extends View
{
    /**
     * ÎÄ±¾
     */
    private String mTitleText;
    /**
     * ÎÄ±¾µÄÑÕÉ«
     */
    private int mTitleTextColor;
    /**
     * ÎÄ±¾µÄ´óÐ¡
     */
    private int mTitleTextSize;

    /**
     * »æÖÆÊ±¿ØÖÆÎÄ±¾»æÖÆµÄ·¶Î§
     */
    private Rect mBound;
    private Paint mPaint;

    public CustomTitleView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context)
    {
        this(context, null);
    }

    /**
     * »ñµÃÎÒ×Ô¶¨ÒåµÄÑùÊ½ÊôÐÔ
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomTitleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        /**
         * »ñµÃÎÒÃÇËù¶¨ÒåµÄ×Ô¶¨ÒåÑùÊ½ÊôÐÔ
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    // Ä¬ÈÏÑÕÉ«ÉèÖÃÎªºÚÉ«
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    // Ä¬ÈÏÉèÖÃÎª16sp£¬TypeValueÒ²¿ÉÒÔ°Ñsp×ª»¯Îªpx
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }

        }
        a.recycle();

        /**
         * »ñµÃ»æÖÆÎÄ±¾µÄ¿íºÍ¸ß
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        // mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);

        this.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                mTitleText = randomText();
                postInvalidate();
            }

        });

    }
    private String randomText()
    {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }

        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        /**
         * ÉèÖÃ¿í¶È
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:// Ã÷È·Ö¸¶¨ÁË
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:// Ò»°ãÎªWARP_CONTENT
                width = getPaddingLeft() + getPaddingRight() + mBound.width();
                break;
        }

        /**
         * ÉèÖÃ¸ß¶È
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:// Ã÷È·Ö¸¶¨ÁË
                height = getPaddingTop() + getPaddingBottom() + specSize;
                break;
            case MeasureSpec.AT_MOST:// Ò»°ãÎªWARP_CONTENT
                height = getPaddingTop() + getPaddingBottom() + mBound.height();
                break;
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }
}
