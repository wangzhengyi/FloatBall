package com.wzy.aball.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.wzy.aball.R;
import com.wzy.aball.engine.FloatViewManager;

public class FloatMenuLayout extends LinearLayout {

    private LinearLayout mMainLayout;
    private TranslateAnimation mTranslateAnimation;

    public FloatMenuLayout(Context context) {
        this(context, null);
    }

    public FloatMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.view_float_menu, this, false);
        mMainLayout = (LinearLayout) view.findViewById(R.id.id_main_layout);
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatViewManager floatViewManager = FloatViewManager.getInstance(getContext());
                WindowManager wm = floatViewManager.getWindowManager();
                wm.removeView(FloatMenuLayout.this);
                floatViewManager.showFloatBall();
                return true;
            }
        });
        addView(view);
    }

    private void initAnimation() {
        mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        mTranslateAnimation.setDuration(500);
        mTranslateAnimation.setFillAfter(true);
    }

    public void startTransAnimation() {
        initAnimation();
        mMainLayout.startAnimation(mTranslateAnimation);
    }
}
