package com.itheima.topline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.topline.R;
import com.itheima.topline.bean.VideoDetailBean;

import java.util.List;

public class VideoDetailListAdapter extends BaseAdapter {
    private Context mContext;
    private List<VideoDetailBean> vdbl;
    private int selectedPosition = -1;//点击时选中的位置
    private OnSelectListener onSelectListener;
    public VideoDetailListAdapter(Context context, OnSelectListener onSelectListener) {
        this.mContext = context;
        this.onSelectListener = onSelectListener;
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
    /**
     * 设置数据 更新界面
     */
    public void setData(List<VideoDetailBean> vdbl) {
        this.vdbl = vdbl;
        notifyDataSetChanged();
    }
    /**
     * 获取Item的总数
     */
    @Override
    public int getCount() {
        return vdbl == null ? 0 : vdbl.size();
    }
    /**
     * 根据position得到对应Item的对象
     */
    @Override
    public VideoDetailBean getItem(int position) {
        return vdbl == null ? null : vdbl.get(position);
    }
    /**
     * 根据position得到对应Item的id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * 得到相应position对应的Item视图，参数position是当前Item的位置，
     * 参数convertView就是滑出屏幕的Item的View
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        //复用convertView
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.video_detail_item, null);
            vh.title = (TextView) convertView.findViewById(R.id.tv_video_name);
            vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //获取position对应的Item的数据对象
        final VideoDetailBean bean = getItem(position);
        vh.title.setTextColor(mContext.getResources().getColor(R.color.
                video_detail_text_color));
        if (bean != null) {
            vh.title.setText(bean.getVideo_name());
            //设置选中效果
            if (selectedPosition == position) {
                vh.iv_icon.setImageResource(R.drawable.iv_video_selected_icon);
                vh.title.setTextColor(mContext.getResources().getColor(R.color.
                        rdTextColorPress));
            } else {
                vh.iv_icon.setImageResource(R.drawable.iv_video_icon);
                vh.title.setTextColor(mContext.getResources().getColor(R.color.
                        video_detail_text_color));
            }
        }
        //每个Item的点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到习题详情界面
                if (bean == null) {
                    return;
                }
                //播放视频
                onSelectListener.onSelect(position, vh.iv_icon);
            }
        });
        return convertView;
    }
    class ViewHolder {
        public TextView title;
        public ImageView iv_icon;
    }
    /**
     * 创建OnSelectListener接口把位置position和控件ImageView传递到Activity界面
     */
    public interface OnSelectListener {
        void onSelect(int position, ImageView iv);
    }
}
