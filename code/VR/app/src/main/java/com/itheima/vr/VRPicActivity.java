package com.itheima.vr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.InputStream;

public class VRPicActivity extends AppCompatActivity {
    private VrPanoramaView vrImage;
    private AsyncTask<Void, Void, Bitmap> task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrpic);
        init();
    }
    private void init() {
        vrImage = (VrPanoramaView) findViewById(R.id.vr_image);
        vrImage.setEventListener(new VrPanoramaEventListener());//处理图片加载出错的情况
        //2.异步加载全景图片
        task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    // 从assets目录中加载图片
                    InputStream is = getAssets().open("andes.jpg");
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    return bitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) {
                    //加载配置
                    VrPanoramaView.Options options = new VrPanoramaView.Options();
                    //设置图片为立体效果
                    options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
                    //加载图片
                    vrImage.loadImageFromBitmap(bitmap, options);
                }
            }
        }.execute();
    }
    @Override
    protected void onResume() {
        super.onResume();
        vrImage.resumeRendering();//恢复渲染
    }
    @Override
    protected void onPause() {
        super.onPause();
        vrImage.pauseRendering();//暂停渲染
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        vrImage.shutdown();     //关闭渲染释放内存
        if (task != null) {
            task.cancel(true); //停止异步任务
            task = null;
        }
    }
}
