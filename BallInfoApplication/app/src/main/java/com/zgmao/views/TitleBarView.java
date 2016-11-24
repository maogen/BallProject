package com.zgmao.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：
 * 创建人：mzg
 * 创建时间：2016/11/24 14:28
 * 修改人：mzg
 * 修改时间：2016/11/24 14:28
 * 修改备注：
 */

public class TitleBarView extends RelativeLayout implements View.OnClickListener {
    private ImageView imageBack;// 返回按钮
    private TextView textTitle;// 标题

    public TitleBarView(Context context) {
        super(context);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化布局
     */
    @Override
    protected void onFinishInflate() {
        imageBack = (ImageView) findViewById(R.id.image_title_back);
        textTitle = (TextView) findViewById(R.id.text_product_title);
        // 设置点击事件
        imageBack.setOnClickListener(this);
        super.onFinishInflate();
    }

    /**
     * 设置是否有返回键
     *
     * @param isShow 是否显示
     */
    public void setImageBackShow(boolean isShow) {
        if (!isShow) {
            imageBack.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        textTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_title_back:
                // 返回键
                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity) context).setResult(Activity.RESULT_OK);
                    ((Activity) context).finish();
                }
                break;
        }
    }
}
