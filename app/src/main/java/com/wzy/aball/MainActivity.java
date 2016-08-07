package com.wzy.aball;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wzy.aball.service.BackViewService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.id_btn);
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String info = "Product Model: " + android.os.Build.MODEL + ","
                        + Build.VERSION.SDK_INT + ","
                        + android.os.Build.VERSION.RELEASE;
                Log.e(TAG, "onClick: " + info);
                startFloatBall();
            }
        });
    }

    private void startFloatBall() {
        Intent intent = new Intent(this, BackViewService.class);
        startService(intent);
        finish();
    }
}
