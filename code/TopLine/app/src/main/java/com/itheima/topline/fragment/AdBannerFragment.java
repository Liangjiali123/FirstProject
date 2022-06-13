package com.itheima.topline.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.itheima.topline.R;
import com.itheima.topline.activity.NewsDetailActivity;
import com.itheima.topline.bean.NewsBean;
public class AdBannerFragment extends Fragment {
    private NewsBean nb;   //广告
    private ImageView iv;  //图片
    public static AdBannerFragment newInstance(Bundle args) {
        AdBannerFragment af = new AdBannerFragment();
        af.setArguments(args);
        return af;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        nb = (NewsBean) arg.getSerializable("ad"); //获取一个新闻对象
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (nb != null) {
            //调用Glide框架加载图片
            Glide
                    .with(getActivity())
                    .load(nb.getImg1())
                    .error(R.mipmap.ic_launcher)
                    .into(iv);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        iv = new ImageView(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        iv.setLayoutParams(lp);                           //设置图片宽高参数
        iv.setScaleType(ImageView.ScaleType.FIT_XY); //把图片填满整个控件
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nb == null) return;
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("newsBean", nb);
                getActivity().startActivity(intent);
            }
        });
        return iv;
    }
}
