package com.zgmao.listener;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.maf.utils.LogUtils;
import com.maf.utils.StringUtils;
import com.zgmao.utils.ViewUtils;

/**
 * 项目名称：BallInfoApplication
 * 类描述：
 * 创建人：mzg
 * 创建时间：2016/12/6 14:36
 * 修改人：mzg
 * 修改时间：2016/12/6 14:36
 * 修改备注：
 */

public class MyEditTextWatcher implements TextWatcher {
    private Activity activity;

    private EditText editText;// 当前监听的输入框
    private EditText nextEdit;// 下一个需要聚焦的输入框

    public MyEditTextWatcher(Activity activity, EditText editText, EditText nextEdit) {
        this.activity = activity;
        this.editText = editText;
        this.nextEdit = nextEdit;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String content = charSequence.toString();
        if (StringUtils.isNotEmpty(content)
                && content.length() >= 2) {
            if (nextEdit != null) {
                nextEdit.requestFocus();
            } else {
                // 最后一个，隐藏输入法
                ViewUtils.hideInputMethodManager(activity);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
