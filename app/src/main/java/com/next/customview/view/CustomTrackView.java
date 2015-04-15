package com.next.customview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.next.customview.R;

/**
 * Created by NeXT on 15-4-15.
 */
public class CustomTrackView extends View {

    private int mFirstColor;
    private int mSecondColor;

    private Paint mPaint;
    private Paint mSmallCirclePaint;
    private boolean isNext = false;

    private int mDistance = 10;
    private int mProgress;

    int leftPoint;
    int rightPoint;
    int topPoint;
    int bottomPoint;

    int trackWidth;
    int trackHeight ;
    int quarterCircle;
    int trackPerimeter;
    int startX;

    public CustomTrackView(Context context) {
        this(context, null);
    }
    public CustomTrackView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CustomTrackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTrackView,
                defStyleAttr,
                0
        );

        mFirstColor = a.getColor(a.getIndex(R.styleable.CustomTrackView_firstColor), Color.BLUE);
        mSecondColor = a.getColor(a.getIndex(R.styleable.CustomTrackView_secondColor), Color.GREEN);

        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(mFirstColor);

        mSmallCirclePaint = new Paint();
        mSmallCirclePaint.setAntiAlias(true);
        mSmallCirclePaint.setStyle(Paint.Style.STROKE);
        mSmallCirclePaint.setStrokeWidth(8);
        mSmallCirclePaint.setColor(Color.BLACK);


        new Thread() {
            @Override
            public void run() {
                while (true) {
                    mDistance = mDistance + 1;
                    postInvalidate();
                    try{
                        Thread.sleep(20);   //速度
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.i("ZZZ", "zzzzzzzzz");
        Log.i("ZZZ", "getWidth() : " + getWidth() + " --- " + "getHeight() : " + getHeight());
        leftPoint = getPaddingLeft();
        rightPoint = getWidth() - getPaddingRight();
        topPoint = getPaddingTop();
        bottomPoint = getHeight() - getPaddingBottom();

        Log.i("ZZZ", "bottomPoint : " + bottomPoint + " --- topPoint : " + topPoint);
        trackWidth = rightPoint - leftPoint - 120;
        trackHeight = bottomPoint - topPoint - 120;
        quarterCircle = (int)(2 * Math.PI * 60 / 4);
        trackPerimeter = (trackWidth + trackHeight) * 2 + quarterCircle * 4;
        startX = getWidth() / 2;

        Log.i("onDraw", "trackWidth : " + trackWidth);
        Log.i("onDraw", "trackHeight : " + trackHeight);
        Log.i("onDraw", "quarterCircle : " + quarterCircle);
        Log.i("onDraw", "trackPerimeter : " + trackPerimeter);
        /*
        Log.i("OnDraw", "左 右 上 下 " +
                leftPoint + " - " +
                rightPoint + " - " +
                topPoint + " - " +
                bottomPoint);
        */
//        Log.i("OnDraw" ,  "rx : "+(rightPoint - leftPoint) / 2 + "  ry : " +  (bottomPoint - topPoint) / 2);
        //rx : 364  ry : 491

        leftPoint = getPaddingLeft();
        rightPoint = getWidth() - getPaddingRight();
        topPoint = getPaddingTop();
        bottomPoint = getHeight() - getPaddingBottom();


        @SuppressLint("DrawAllocation")
        RectF rectF = new RectF(leftPoint, topPoint, rightPoint, bottomPoint);

        canvas.drawRect(rectF, mPaint);

        canvas.drawRoundRect(rectF, 60, 60, mPaint);


        @SuppressLint("DrawAllocation")
        RectF rectF1 = new RectF(leftPoint, topPoint, leftPoint + 120, topPoint + 120);
        canvas.drawArc(rectF1, 180, 90, false, mSmallCirclePaint);

//        int leftTopX = (int)rectF1.centerX() - 60;
//        int leftTopY = (int)rectF1.centerY();
//        canvas.drawLine(leftTopX, leftTopY, leftTopX, bottomPoint - 60, mSmallCirclePaint);

//        canvas.drawArc(rectF, -90, 180, false, mPaint);    //根据进度画圆弧
//        canvas.drawArc(rectF, 0, 360, false, mPaint);
//        canvas.drawArc(rectF, 180, 180, false, mPaint);

//        canvas.drawPoint(leftTopX, bottomPoint - 60, mSmallCirclePaint);

        int mDistance1 = mDistance % trackPerimeter;
        if (trackWidth / 2 > mDistance1 && mDistance1 > 0) {
            @SuppressLint("DrawAllocation")
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            canvas.drawBitmap(getRoundedCornerBitmap(bitmap, 90), startX + mDistance1 - bitmap.getWidth() / 2,
                    bottomPoint - bitmap.getHeight() / 2, mPaint);
//            canvas.drawBitmap(getRoundedCornerBitmap(bitmap, 90), leftTopX - bitmap.getWidth() / 2,
//                    bottomPoint - 60 - bitmap.getHeight() / 2, mPaint);
        }

    }

    //获得圆角图片的方法
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}
