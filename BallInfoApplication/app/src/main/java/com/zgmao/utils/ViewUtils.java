package com.zgmao.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.maf.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名称：Ytb_Android
 * 类描述：处理VIew的工具类
 * 创建人：mzg
 * 创建时间：2016/7/26 11:45
 * 修改人：mzg
 * 修改时间：2016/7/26 11:45
 * 修改备注：
 */
public class ViewUtils {
    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    public static void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<>();
        View child;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /**
     * 调整numberpicker大小
     */
    private static void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 0, 5, 0);
        np.setLayoutParams(params);
    }

    /**
     * 设置编辑框是否可编辑
     *
     * @param editText 编辑框
     * @param able     是否可编辑
     */
    public static void setEditAble(EditText editText, boolean able) {
        editText.setEnabled(able);
        editText.setFocusableInTouchMode(able);
        editText.setFocusable(able);
        if (able) {
            editText.requestFocus();
            String content = editText.getText().toString();
            if (StringUtils.isNotEmpty(content)) {
                editText.setSelection(content.length());
            }
        }
    }


    /**
     * 显示输入法
     *
     * @param editText
     */
    public static void showInputMethodManager(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(editText, 0);
                           }
                       },
                200);
    }

    /**
     * 隐藏输入法
     *
     * @param activity
     */
    public static void hideInputMethodManager(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (imm != null && v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 判断点击是否是指定的VIew
     *
     * @param view
     * @param ev
     * @return
     */
    public static boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }
}
