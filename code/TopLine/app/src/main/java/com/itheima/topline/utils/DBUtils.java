package com.itheima.topline.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.topline.bean.ConstellationBean;
import com.itheima.topline.bean.NewsBean;
import com.itheima.topline.bean.UserBean;
import com.itheima.topline.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    private static DBUtils instance = null;
    private static SQLiteHelper helper;
    private static SQLiteDatabase db;
    public DBUtils(Context context) {
        helper = new SQLiteHelper(context);
        db = helper.getWritableDatabase();
    }
    public static DBUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DBUtils(context);
        }
        return instance;
    }
    /*
 * 根据登录名获取用户头像
 */
    public String getUserHead(String userName) {
        String sql = "SELECT head FROM " + SQLiteHelper.U_USERINFO + " WHERE userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        String head = "";
        while (cursor.moveToNext()) {
            head = cursor.getString(cursor.getColumnIndex("head"));
        }
        cursor.close();
        return head;
    }
    /**
     * 保存个人资料信息
     */
    public void saveUserInfo(UserBean bean) {
        ContentValues cv = new ContentValues();
        cv.put("userName", bean.getUserName());
        cv.put("nickName", bean.getNickName());
        cv.put("sex", bean.getSex());
        cv.put("signature", bean.getSignature());
        db.insert(SQLiteHelper.U_USERINFO, null, cv);
    }
    /**
     * 获取个人资料信息
     */
    public UserBean getUserInfo(String userName) {
        String sql = "SELECT * FROM " + SQLiteHelper.U_USERINFO + " WHERE userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        UserBean bean = null;
        while (cursor.moveToNext()) {
            bean = new UserBean();
            bean.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            bean.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
            bean.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            bean.setSignature(cursor.getString(cursor.getColumnIndex("signature")));
            bean.setHead(cursor.getString(cursor.getColumnIndex("head")));
        }
        cursor.close();
        return bean;
    }
    /**
     * 修改个人资料
     */
    public void updateUserInfo(String key, String value, String userName) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        db.update(SQLiteHelper.U_USERINFO, cv, "userName=?",new String[]{userName});
    }
    /**
     * 保存十二星座信息
     */
    public void saveConstellationInfo(List<ConstellationBean> list) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteHelper.CONSTELLATION, null);
        if (cursor.getCount() != 0)//添加数据时，如果星座表中有数据，则在添加新数据之前需删除旧数据
        {
            //删除表中的数据
            db.execSQL("DELETE FROM " + SQLiteHelper.CONSTELLATION);
        }
        for (ConstellationBean bean : list) {
            ContentValues cv = new ContentValues();
            cv.put("c_id", bean.getId());
            cv.put("name", bean.getName());
            cv.put("head", bean.getHead());
            cv.put("img", bean.getImg());
            cv.put("icon", bean.getIcon());
            cv.put("date", bean.getDate());
            cv.put("info", bean.getInfo());
            cv.put("whole", bean.getWhole());
            cv.put("love", bean.getLove());
            cv.put("career", bean.getCareer());
            cv.put("money", bean.getMoney());
            cv.put("whole_info", bean.getWhole_info());
            cv.put("love_info", bean.getLove_info());
            cv.put("career_info", bean.getCareer_info());
            cv.put("money_info", bean.getMoney_info());
            cv.put("health_info", bean.getHealth_info());
            db.insert(SQLiteHelper.CONSTELLATION, null, cv);
        }
    }
    /**
     * 根据id获取星座信息
     */
    public ConstellationBean getConstellationInfo(int c_id) {
        String sql = "SELECT * FROM " + SQLiteHelper.CONSTELLATION + " WHERE c_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{c_id + ""});
        ConstellationBean bean = null;
        while (cursor.moveToNext()) {
            bean = new ConstellationBean();
            bean.setName(cursor.getString(cursor.getColumnIndex("name")));
            bean.setHead(cursor.getString(cursor.getColumnIndex("head")));
            bean.setImg(cursor.getString(cursor.getColumnIndex("img")));
            bean.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            bean.setDate(cursor.getString(cursor.getColumnIndex("date")));
            bean.setInfo(cursor.getString(cursor.getColumnIndex("info")));
            bean.setWhole(cursor.getInt(cursor.getColumnIndex("whole")));
            bean.setLove(cursor.getInt(cursor.getColumnIndex("love")));
            bean.setCareer(cursor.getInt(cursor.getColumnIndex("career")));
            bean.setMoney(cursor.getInt(cursor.getColumnIndex("money")));
            bean.setWhole_info(cursor.getString(cursor.getColumnIndex("whole_info")));
            bean.setLove_info(cursor.getString(cursor.getColumnIndex("love_info")));
            bean.setCareer_info(cursor.getString(cursor.getColumnIndex("career_info")));
            bean.setMoney_info(cursor.getString(cursor.getColumnIndex("money_info")));
            bean.setHealth_info(cursor.getString(cursor.getColumnIndex("health_info")));
        }
        cursor.close();
        return bean;
    }
    /**
     * 保存收藏信息
     */
    public void saveCollectionNewsInfo(NewsBean bean, String userName) {
        ContentValues cv = new ContentValues();
        cv.put("id", bean.getId());
        cv.put("type", bean.getType());
        cv.put("userName", userName);
        cv.put("newsName", bean.getNewsName());
        cv.put("newsTypeName", bean.getNewsTypeName());
        cv.put("img1", bean.getImg1());
        cv.put("img2", bean.getImg2());
        cv.put("img3", bean.getImg3());
        cv.put("newsUrl", bean.getNewsUrl());
        db.insert(SQLiteHelper.COLLECTION_NEWS_INFO, null, cv);
    }
    /**
     * 获取收藏信息
     */
    public List<NewsBean> getCollectionNewsInfo(String userName) {
        String sql = "SELECT * FROM " + SQLiteHelper.COLLECTION_NEWS_INFO
                + " WHERE  userName=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        List<NewsBean> newsList = new ArrayList<>();
        NewsBean bean = null;
        while (cursor.moveToNext()) {
            bean = new NewsBean();
            bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            bean.setType(cursor.getInt(cursor.getColumnIndex("type")));
            bean.setNewsName(cursor.getString(cursor.getColumnIndex("newsName")));
            bean.setNewsTypeName(cursor.getString(cursor.getColumnIndex("newsTypeName")));
            bean.setImg1(cursor.getString(cursor.getColumnIndex("img1")));
            bean.setImg2(cursor.getString(cursor.getColumnIndex("img2")));
            bean.setImg3(cursor.getString(cursor.getColumnIndex("img3")));
            bean.setNewsUrl(cursor.getString(cursor.getColumnIndex("newsUrl")));
            newsList.add(bean);
        }
        cursor.close();
        return newsList;
    }
    /**
     * 判断一条新闻是否被收藏
     */
    public boolean hasCollectionNewsInfo(int id, int type, String userName) {
        boolean hasNewsInfo = false;
        String sql = "SELECT * FROM " + SQLiteHelper.COLLECTION_NEWS_INFO
                + " WHERE id=? AND type=? AND userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id + "", type + "", userName + ""});
        if (cursor.moveToFirst()) {
            hasNewsInfo = true;
        }
        cursor.close();
        return hasNewsInfo;
    }
    /**
     * 删除某一条收藏信息
     */
    public boolean delCollectionNewsInfo(int id, int type, String userName) {
        boolean delSuccess = false;
        if (hasCollectionNewsInfo(id, type, userName)) {
            int row = db.delete(SQLiteHelper.COLLECTION_NEWS_INFO,
                    " id=? AND type=? AND userName=? ", new String[]{id + "", type + "", userName});
            if (row > 0) {
                delSuccess = true;
            }
        }
        return delSuccess;
    }

}
