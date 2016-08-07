package com.wzy.aball.engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.wzy.aball.view.FloatBallView;

public class FloatViewManager {
    private static final String TAG = FloatViewManager.class.getSimpleName();
    private static final float MOVE_DISTANCE = 50;
    private static FloatViewManager mInstance;
    private final int mWindowWidth;
    private Context mContext;
    private WindowManager mWindowManager;
    private FloatBallView mFloatBallView;
    private float mStartX;
    private float mStartY;
    private WindowManager.LayoutParams params;
    private float mInitX;


    private FloatViewManager(Context context) {
        this.mContext = context;
        this.mWindowWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.mFloatBallView = new FloatBallView(context);
        this.mFloatBallView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mInitX = event.getRawX();
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        mFloatBallView.setDragState(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        mFloatBallView.setDragState(false);
                        float upx = event.getRawX();
                        if (upx <= mWindowWidth / 2) {
                            upx = 0;
                        } else {
                            upx = mWindowWidth - FloatBallView.FLOAT_BALL_WIDTH;
                        }
                        params.x = (int) upx;
                        mWindowManager.updateViewLayout(mFloatBallView, params);
                        if (upx - mInitX > MOVE_DISTANCE) {
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mFloatBallView.setDragState(true);
                        float x = event.getRawX();
                        float y = event.getRawY();
                        float dx = x - mStartX;
                        float dy = y - mStartY;
                        params.x += dx;
                        params.y += dy;
                        mWindowManager.updateViewLayout(mFloatBallView, params);
                        mStartX = x;
                        mStartY = y;
                        break;
                }
                return false;
            }
        });
        this.mFloatBallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击小球", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static FloatViewManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FloatViewManager.class) {
                if (mInstance == null) {
                    mInstance = new FloatViewManager(context);
                }
            }
        }
        return mInstance;
    }

    public void showFloatBall() {
        params = new WindowManager.LayoutParams();
        params.width = FloatBallView.FLOAT_BALL_WIDTH;
        params.height = FloatBallView.FLOAT_BALL_HEIGHT;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;

        Log.e(TAG, "showFloatBall: width=" + params.width + ", height = " + params.height);
        mWindowManager.addView(mFloatBallView, params);
    }
}
