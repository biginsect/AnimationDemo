package com.biginsect.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ZoomTransImageView mIvSwitch;
    private Button mBtnAnim;
    private ZoomTransImageView mCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvSwitch = findViewById(R.id.iv_switch);
        mCollect = findViewById(R.id.iv_collect);
        mBtnAnim = findViewById(R.id.btn_anim);

        mIvSwitch.addSwitchResource(R.drawable.ic_video_audio, R.drawable.ic_video_video);
        mCollect.addSwitchResource(R.drawable.ic_big_collect_0, R.drawable.ic_big_collect_1);

        final PressZoomController pressZoomController = new PressZoomController(mBtnAnim);

        mBtnAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvSwitch.playAnim();
                mCollect.playAnim();
                pressZoomController.playAnim();
            }
        });
    }
}