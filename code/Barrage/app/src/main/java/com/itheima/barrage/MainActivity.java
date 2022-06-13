package com.itheima.barrage;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.VideoView;
import java.util.Random;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;
public class MainActivity extends AppCompatActivity {
    private boolean showDanmaku;
    private DanmakuView danmakuView;
    private DanmakuContext danmakuContext;
    private Button sendButton;
    private LinearLayout sendLayout;
    private EditText editText;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        playVideo();
        initDanmaku();
    }
    /**
     * 初始化界面控件
     */
    private void initView() {
        videoView = (VideoView) findViewById(R.id.videoview);
        sendLayout = (LinearLayout) findViewById(R.id.ly_send);
        sendButton = (Button) findViewById(R.id.btn_send);
        editText = (EditText) findViewById(R.id.et_text);
        danmakuView = (DanmakuView) findViewById(R.id.danmaku);
    }
    /**
     * 播放视频
     */
    private void playVideo() {
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.sun;
        if (uri != null) {
            videoView.setVideoURI(Uri.parse(uri));
            videoView.start();
        } else {
            videoView.getBackground().setAlpha(0);//将背景设为透明
        }
    }
    /**
     * 创建弹幕解析器
     */
    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };
    /**
     * 初始化弹幕
     */
    private void initDanmaku() {
        danmakuView.setCallback(new DrawHandler.Callback() {//设置回调函数
            @Override
            public void prepared() {
                showDanmaku = true;
                danmakuView.start(); //开始弹幕
                generateDanmakus();  //调用随机生成弹幕方法
            }
            @Override
            public void updateTimer(DanmakuTimer timer) {
            }
            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
            }
            @Override
            public void drawingFinished() {
            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.enableDanmakuDrawingCache(true);//提升屏幕绘制效率
        danmakuView.prepare(parser, danmakuContext);//进行弹幕准备
        //为danmakuView设置点击事件
        danmakuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendLayout.getVisibility() == View.GONE) {
                    sendLayout.setVisibility(View.VISIBLE);//显示布局
                } else {
                    sendLayout.setVisibility(View.GONE);   //隐藏布局
                }
            }
        });
        //为发送按钮设置点击事件
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    addDanmaku(content, true);//添加一条弹幕
                    editText.setText("");
                }
            }
        });
    }
    /**
     * 添加一条弹幕
     *
     * @param content    弹幕的具体内容
     * @param border     弹幕是否有边框
     */
    private void addDanmaku(String content, boolean border) {
        //创建弹幕对象,TYPE_SCROLL_RL表示从右向左滚动的弹幕
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(
                BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 6;
        danmaku.textSize = 30;
        danmaku.textColor = Color.WHITE;//弹幕文字的颜色
        danmaku.setTime(danmakuView.getCurrentTime());
        if (border) {
            danmaku.borderColor = Color.BLUE;//弹幕文字边框的颜色
        }
        danmakuView.addDanmaku(danmaku);     //添加一条弹幕
    }
    /**
     * 随机生成一些弹幕内容
     */
    private void generateDanmakus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (showDanmaku) {
                    int num = new Random().nextInt(300);
                    String content = "" + num;
                    addDanmaku(content, false);
                    try {
                        Thread.sleep(num);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() &&
                danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        showDanmaku = false;
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }
}
