package com.wzy.aball.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class BallProgressView extends View {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    private Paint mCirclePaint;
    private Paint mProgressPaint;
    private Paint mTextPaint;
    private Canvas mBitmapCanvas;

    private Path mPath;
    private int mCurrentProgress = 50;
    private int mMaxProgress = 100;
    private Bitmap mBitmap;
    private GestureDetector mGestureDetector;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private int mInitProgress = 0;
    private Runnable mDoubleTapTask = new Runnable() {
        @Override
        public void run() {
            mInitProgress ++;
            if (mInitProgress < mCurrentProgress) {
                invalidate();
                mHandle.postDelayed(mDoubleTapTask, 50);
            } else {
                mHandle.removeCallbacks(mDoubleTapTask);
                mInitProgress = 0;
            }
        }
    };
    private int count = 50;
    private Runnable mSingleTapTask = new Runnable() {
        @Override
        public void run() {
            count --;
            if (count >= 0) {
                invalidate();
                mHandle.postDelayed(mSingleTapTask, 200);
            } else {
                mHandle.removeCallbacks(mSingleTapTask);
                count = 50;
            }
        }
    };

    private boolean isSingleTap;

    public BallProgressView(Context context) {
        this(context, null);
    }

    public BallProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
        initCanvas();
        initPath();
        initGestureDetector();
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        isSingleTap = false;
                        Toast.makeText(getContext(), "双击", Toast.LENGTH_LONG).show();
                        startDoubleTapAnimation();
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        Toast.makeText(getContext(), "单击", Toast.LENGTH_LONG).show();
                        isSingleTap = true;
                        startSingleTapAnimation();
                        return super.onSingleTapConfirmed(e);
                    }
                });
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        setClickable(true);
    }

    private void startSingleTapAnimation() {
        mHandle.postDelayed(mSingleTapTask, 200);
    }

    private void initPath() {
        mPath = new Path();
    }

    private void initPaints() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#3a8c6c"));

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(Color.parseColor("#4ec963"));
        mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initCanvas() {
        mBitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(WIDTH, HEIGHT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw circle
        mBitmapCanvas.drawCircle(WIDTH / 2, HEIGHT / 2, WIDTH / 2, mCirclePaint);

        // draw progress
        int high;
        if (!isSingleTap) {
            high = (int) (mInitProgress * 1.0f / mMaxProgress * HEIGHT);
        } else {
            high = (int) (mCurrentProgress * 1.0f / mMaxProgress * HEIGHT);
        }
        high = HEIGHT - high;
        mPath.reset();
        mPath.moveTo(WIDTH, high);
        mPath.lineTo(WIDTH, HEIGHT);
        mPath.lineTo(0, HEIGHT);
        mPath.lineTo(0, high);
        // 波浪线使用二阶贝塞尔曲线
        if (!isSingleTap) {
            float d = mInitProgress * 1.0f / mCurrentProgress;
            d = 1 - d;
            for (int i = 0; i < WIDTH / 40; i++) {
                mPath.rQuadTo(10, -10 * d, 20, 0);
                mPath.rQuadTo(10, 10 * d, 20, 0);
            }
            mPath.close();
        } else {
            float dis = count * 1.0f / 50 * 10;
            if (count % 2 == 0) {
                for (int i = 0; i < 5; i ++) {
                    mPath.rQuadTo(20, -dis, 40, 0);
                    mPath.rQuadTo(20, dis, 40, 0);
                }
            } else {
                for (int i = 0; i < 5; i ++) {
                    mPath.rQuadTo(20, dis, 40, 0);
                    mPath.rQuadTo(20, -dis, 40, 0);
                }
            }
        }
        mBitmapCanvas.drawPath(mPath, mProgressPaint);

        // draw text
        Paint.FontMetricsInt fmi = mTextPaint.getFontMetricsInt();
        int percent = (int) (mCurrentProgress * 1.0f / mMaxProgress);
        String text = (percent * 100) + "%";
        mBitmapCanvas.drawText(text, WIDTH / 2, (HEIGHT - fmi.descent - fmi.ascent) / 2, mTextPaint);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }


    private void startDoubleTapAnimation() {
        mHandle.postDelayed(mDoubleTapTask, 50);
    }
}
