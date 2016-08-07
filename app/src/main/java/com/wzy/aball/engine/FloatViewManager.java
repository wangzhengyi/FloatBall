package com.wzy.aball.engine;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.wzy.aball.view.FloatBallView;
import com.wzy.aball.view.FloatMenuLayout;

import java.lang.reflect.Field;

public class FloatViewManager {
    private static final String TAG = FloatViewManager.class.getSimpleName();
    private static final float MOVE_DISTANCE = 50;

    private static FloatViewManager mInstance;

    private final int mWindowWidth;
    private final int mWindowHeight;
    private final int mStatusBarHeight;

    private Context mContext;
    private WindowManager mWindowManager;

    private FloatBallView mFloatBallView;
    private WindowManager.LayoutParams mFloatBallLayoutParams;
    private FloatMenuLayout mFloatMenuLayout;
    private WindowManager.LayoutParams mFloatMenuLayoutParams;

    private float mInitDownX;
    private float mTouchDownX;
    private float mTouchDownY;

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

    private FloatViewManager(Context context) {
        this.mContext = context;
        this.mWindowWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.mWindowHeight = context.getResources().getDisplayMetrics().heightPixels;
        this.mStatusBarHeight = getStatusBarHeight();
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.mFloatBallView = new FloatBallView(context);
        this.mFloatBallView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFloatBallView.setDragState(true);
                        mInitDownX = event.getRawX();
                        mTouchDownX = event.getRawX();
                        mTouchDownY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mFloatBallView.setDragState(false);
                        float upx = event.getRawX();
                        if (upx <= mWindowWidth / 2) {
                            upx = 0;
                        } else {
                            upx = mWindowWidth - FloatBallView.FLOAT_BALL_WIDTH;
                        }
                        mFloatBallLayoutParams.x = (int) upx;
                        mWindowManager.updateViewLayout(mFloatBallView, mFloatBallLayoutParams);
                        if (upx - mInitDownX > MOVE_DISTANCE) {
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mFloatBallView.setDragState(true);
                        float x = event.getRawX();
                        float y = event.getRawY();
                        float dx = x - mTouchDownX;
                        float dy = y - mTouchDownY;
                        mFloatBallLayoutParams.x += dx;
                        mFloatBallLayoutParams.y += dy;
                        mWindowManager.updateViewLayout(mFloatBallView, mFloatBallLayoutParams);
                        mTouchDownX = x;
                        mTouchDownY = y;
                        break;
                }
                return false;
            }
        });
        this.mFloatBallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击小球", Toast.LENGTH_SHORT).show();
                mWindowManager.removeView(mFloatBallView);
                showFloatMenuLayout();
            }
        });

        mFloatMenuLayout = new FloatMenuLayout(mContext);
    }

    private void showFloatMenuLayout() {
        if (mFloatMenuLayoutParams == null) {
            mFloatMenuLayoutParams = new WindowManager.LayoutParams();
            mFloatMenuLayoutParams.width = mWindowWidth;
            mFloatMenuLayoutParams.height = mWindowHeight - mStatusBarHeight;
            Log.e(TAG, "showFloatMenuLayout: width=" + mFloatMenuLayoutParams.width + ", height=" + mFloatMenuLayoutParams.height
                    + ", Screen height=" + mWindowHeight + ", status height=" + mStatusBarHeight);
            mFloatMenuLayoutParams.gravity = Gravity.BOTTOM | Gravity.START;
            mFloatMenuLayoutParams.x = 0;
            mFloatMenuLayoutParams.y = 0;
            mFloatMenuLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            mFloatMenuLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            mFloatMenuLayoutParams.format = PixelFormat.RGBA_8888;
        }

        if (mFloatMenuLayout.getParent() != null) {
            mWindowManager.removeView(mFloatMenuLayout);
        }
        mWindowManager.addView(mFloatMenuLayout, mFloatMenuLayoutParams);
        mFloatMenuLayout.startTransAnimation();
    }

    public void showFloatBall() {
        if (mFloatBallLayoutParams == null) {
            mFloatBallLayoutParams = new WindowManager.LayoutParams();
            mFloatBallLayoutParams.width = FloatBallView.FLOAT_BALL_WIDTH;
            mFloatBallLayoutParams.height = FloatBallView.FLOAT_BALL_HEIGHT;
            mFloatBallLayoutParams.gravity = Gravity.TOP | Gravity.START;
            mFloatBallLayoutParams.x = 0;
            mFloatBallLayoutParams.y = 0;
            mFloatBallLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            mFloatBallLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            mFloatBallLayoutParams.format = PixelFormat.RGBA_8888;
        }
        Log.e(TAG, "showFloatBall: width=" + mFloatBallLayoutParams.width + ", height = " + mFloatBallLayoutParams.height);
        if (mFloatBallView.getParent() != null) {
            mWindowManager.removeView(mFloatBallView);
        }
        mWindowManager.addView(mFloatBallView, mFloatBallLayoutParams);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public int getStatusBarHeight() {
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int x = (int) field.get(obj);
            return mContext.getResources().getDimensionPixelSize(x);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | InstantiationException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public WindowManager getWindowManager() {
        return mWindowManager;
    }
}
