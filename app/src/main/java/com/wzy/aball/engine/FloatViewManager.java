package com.wzy.aball.engine;

import android.content.Context;
import android.view.WindowManager;

public class FloatViewManager {
    private static FloatViewManager mInstance;
    private Context mContext;
    private WindowManager mWindowManager;

    private FloatViewManager(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
}
