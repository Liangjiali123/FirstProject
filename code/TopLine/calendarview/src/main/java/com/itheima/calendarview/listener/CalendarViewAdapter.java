package com.itheima.calendarview.listener;

import android.view.View;
import android.widget.TextView;
import com.itheima.calendarview.DateBean;

public interface CalendarViewAdapter {
    /**
     * 返回阳历、阴历两个TextView
     *
     * @param view
     * @param date
     * @return
     */
    TextView[] convertView(View view, DateBean date);
}
