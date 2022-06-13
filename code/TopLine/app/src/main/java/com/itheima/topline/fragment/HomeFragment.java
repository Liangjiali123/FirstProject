package com.itheima.topline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.PullToRefreshView;
import com.itheima.topline.R;
import com.itheima.topline.activity.PythonActivity;
import com.itheima.topline.adapter.AdBannerAdapter;
import com.itheima.topline.adapter.HomeListAdapter;
import com.itheima.topline.bean.NewsBean;
import com.itheima.topline.utils.Constant;
import com.itheima.topline.utils.JsonParse;
import com.itheima.topline.utils.UtilsHelper;
import com.itheima.topline.view.ViewPagerIndicator;
import com.itheima.topline.view.WrapRecyclerView;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class HomeFragment extends Fragment {
    private PullToRefreshView mPullToRefreshView;
    private WrapRecyclerView recycleView;
    public static final int REFRESH_DELAY = 1000;
    private ViewPager adPager;         //广告
    private ViewPagerIndicator vpi;  //小圆点
    private TextView tvAdName;        //广告名称
    private View adBannerLay;         //广告条容器
    private AdBannerAdapter ada; //适配器
    public static final int MSG_AD_SLID = 1;  //广告自动滑动
    public static final int MSG_AD_OK = 2;    //获取广告数据
    public static final int MSG_NEWS_OK = 3; //获取新闻数据
    private MHandler mHandler;                  //事件捕获
    private LinearLayout ll_python;
    private OkHttpClient okHttpClient;
    private HomeListAdapter adapter;
    private RelativeLayout rl_title_bar;
    public HomeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        okHttpClient = new OkHttpClient();
        mHandler = new MHandler();
        getADData();
        getNewsData();
        View view = initView(inflater, container);
        return view;
    }
    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rl_title_bar = (RelativeLayout) view.findViewById(R.id.title_bar);
        rl_title_bar.setVisibility(View.GONE);
        recycleView = (WrapRecyclerView) view.findViewById(R.id.recycler_view);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        View headView = inflater.inflate(R.layout.head_view, container, false);
        recycleView.addHeaderView(headView);
        adapter = new HomeListAdapter(getActivity());
        recycleView.setAdapter(adapter);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(
                R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        getADData();
                        getNewsData();
                    }
                }, REFRESH_DELAY);
            }
        });
        mHandler = new MHandler();
        adBannerLay = headView.findViewById(R.id.adbanner_layout);
        adPager = (ViewPager) headView.findViewById(R.id.slidingAdvertBanner);
        vpi = (ViewPagerIndicator) headView.findViewById(R.id.advert_indicator);
        tvAdName = (TextView) headView.findViewById(R.id.tv_advert_title);
        ll_python = (LinearLayout) headView.findViewById(R.id.ll_python);
        adPager.setLongClickable(false);
        ada = new AdBannerAdapter(getActivity().getSupportFragmentManager(),mHandler);
        adPager.setAdapter(ada);
        adPager.setOnTouchListener(ada);
        adPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                if (ada.getSize() > 0) {
                    if (ada.getTitle(index % ada.getSize()) != null) {
                        tvAdName.setText(ada.getTitle(index % ada.getSize()));
                    }
                    vpi.setCurrentPostion(index % ada.getSize());
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        resetSize();
        setListener();
        new AdAutoSlidThread().start();
        return view;
    }
    private void setListener() {
        ll_python.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PythonActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_AD_SLID:
                    if (ada.getCount() > 0) {
                        adPager.setCurrentItem(adPager.getCurrentItem() + 1);
                    }
                    break;
                case MSG_AD_OK:
                    if (msg.obj != null) {
                        String adResult = (String) msg.obj;
                        List<NewsBean> adl = JsonParse.getInstance().
                                getAdList(adResult);
                        if (adl != null) {
                            if (adl.size() > 0) {
                                ada.setData(adl);
                                tvAdName.setText(adl.get(0).getNewsName());
                                vpi.setCount(adl.size());
                                vpi.setCurrentPostion(0);
                            }
                        }
                    }
                    break;
                case MSG_NEWS_OK:
                    if (msg.obj != null) {
                        String newsResult = (String) msg.obj;
                        List<NewsBean> nbl = JsonParse.getInstance().
                                getNewsList(newsResult);
                        if (nbl != null) {
                            if (nbl.size() > 0) {
                                adapter.setData(nbl);
                            }
                        }
                    }
                    break;
            }
        }
    }
    class AdAutoSlidThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mHandler != null)
                    mHandler.sendEmptyMessage(MSG_AD_SLID);
            }
        }
    }
    private void getNewsData() {
        Request request = new Request.Builder().url(Constant.WEB_SITE +
                Constant.REQUEST_NEWS_URL).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_NEWS_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }
    private void getADData() {
        Request request = new Request.Builder().url(Constant.WEB_SITE +
                Constant.REQUEST_AD_URL).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_AD_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }
    /**
     * 计算控件大小
     */
    private void resetSize() {
        int sw = UtilsHelper.getScreenWidth(getActivity());
        int adLheight = sw / 2; //广告条高度
        ViewGroup.LayoutParams adlp = adBannerLay.getLayoutParams();
        adlp.width = sw;
        adlp.height = adLheight;
        adBannerLay.setLayoutParams(adlp);
    }
}
