package com.wzy.aball.engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wzy.aball.view.FloatBallView;

public class FloatViewManager {
    private static final String TAG = FloatViewManager.class.getSimpleName();
    private static FloatViewManager mInstance;
    private Context mContext;
    private WindowManager mWindowManager;
    private FloatBallView mFloatBallView;

    private FloatViewManager(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.mFloatBallView = new FloatBallView(context);
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
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
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
