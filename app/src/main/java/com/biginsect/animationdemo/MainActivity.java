package com.biginsect.animationdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ZoomTransImageView mIvSwitch;
    private PressZoomImageView mBtnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvSwitch = findViewById(R.id.iv_switch);
        mBtnAnim = findViewById(R.id.btn_anim);

        mIvSwitch.addSwitchResource(R.drawable.ic_video_audio, R.drawable.ic_video_video);
        mBtnAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("view", "click");
            }
        });
        mBtnAnim.setOnAnimEndListener(new BaseAnimImageView.OnAnimEndListener() {
            @Override
            public void onAnimEnd() {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
        mIvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}