package com.itheima.vr;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_pic, iv_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init() {
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        iv_video = (ImageView) findViewById(R.id.iv_video);
        iv_pic.setOnClickListener(this);
        iv_video.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pic:
                //跳转到VR全景图片界面
                Intent pic = new Intent(MainActivity.this,VRPicActivity.class);
                startActivity(pic);
                break;
            case R.id.iv_video:
                //跳转到VR全景视频界面
                Intent video = new Intent(MainActivity.this,VRVideoActivity.class);
                startActivity(video);
                break;
        }
    }
}
