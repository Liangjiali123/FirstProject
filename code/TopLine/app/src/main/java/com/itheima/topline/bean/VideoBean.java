package com.itheima.topline.bean;

import java.util.List;

public class VideoBean {
    private int id;           //视频id
    private String name;    //视频名称
    private String intro;  //视频简介
    private String img;    //视频图片
    private List<VideoDetailBean> videoDetailList;  //视频详情中的列表
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public List<VideoDetailBean> getVideoDetailList() {
        return videoDetailList;
    }
    public void setVideoDetailList(List<VideoDetailBean> videoDetailList) {
        this.videoDetailList = videoDetailList;
    }
}
