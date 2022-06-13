package com.itheima.topline.view;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import com.itheima.topline.R;
/**
 * 弹出菜单
 */
public class PlayTopPopupWindow {
    private PopupWindow popupWindow;
    private RadioGroup rgScreenSize;
    public PlayTopPopupWindow(Context context, int height) {
        View view = LayoutInflater.from(context).inflate(R.layout.play_top_menu, null);
        rgScreenSize = findById(R.id.rg_screensize, view);
        popupWindow = new PopupWindow(view, height * 2 / 3, height);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(178, 0, 0, 0)));
    }
    public void setScreenSizeCheckLister(RadioGroup.OnCheckedChangeListener listener) {
        rgScreenSize.setOnCheckedChangeListener(listener);
    }
    public void showAsDropDown(View parent) {
        popupWindow.showAtLocation(parent, Gravity.RIGHT, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }
    public void dismiss() {
        popupWindow.dismiss();
    }
    @SuppressWarnings("unchecked")
    private <T extends View> T findById(int resId, View view) {
        return (T) view.findViewById(resId);
    }
}
