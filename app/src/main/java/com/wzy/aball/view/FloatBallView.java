package com.wzy.aball.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wzy.aball.R;


public class FloatBallView extends View {
    public static final int FLOAT_BALL_WIDTH = 300;
    public static final int FLOAT_BALL_HEIGHT = 300;

    private Paint mBallPaint;
    private Paint mTextPaint;

    private int mWidth;
    private int mHeight;

    private String mPercent = "50%";

    private Bitmap mDragBitmap;
    private boolean isDrag;

    public FloatBallView(Context context) {
        this(context, null);
    }

    public FloatBallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
        initDragBitmap();
    }

    private void initDragBitmap() {
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mingren);
        mDragBitmap =
                Bitmap.createScaledBitmap(srcBitmap, FLOAT_BALL_WIDTH, FLOAT_BALL_HEIGHT, true);
        srcBitmap.recycle();
    }

    private void initPaints() {
        mBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBallPaint.setColor(Color.GRAY);
        mBallPaint.setDither(true);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureHandler(widthMeasureSpec, FLOAT_BALL_WIDTH);
        int height = measureHandler(heightMeasureSpec, FLOAT_BALL_HEIGHT);

        setMeasuredDimension(width, height);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private int measureHandler(int measureSpec, int defaultSize) {
        int res;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            res = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            res = Math.min(defaultSize, size);
        } else {
            res = defaultSize;
        }

        return res;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isDrag) {
            canvas.drawBitmap(mDragBitmap, 0, 0, null);
        } else {
            // draw circle
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mBallPaint);
            // draw text
            drawCenterText(canvas);
        }
    }

    private void drawCenterText(Canvas canvas) {
        float centerX = mWidth / 2;
        Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
        float baselineY = (mHeight - fm.descent - fm.ascent) / 2;
        canvas.drawText(mPercent, centerX, baselineY, mTextPaint);
    }

    public void setDragState(boolean isDrag) {
        this.isDrag = isDrag;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setPercent(int percent) {
        mPercent = String.valueOf(percent);
        invalidate();
    }
}
