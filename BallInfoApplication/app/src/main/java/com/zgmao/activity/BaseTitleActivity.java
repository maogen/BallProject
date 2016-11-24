package com.zgmao.activity;

import android.os.Bundle;
import android.view.View;

import com.maf.activity.BaseActivity;
import com.maf.activity.BaseCustomSwipeRefreshActivity;
import com.zgmao.views.TitleBarView;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：包含标题的公用界面，需要在布局中包含 <include layout="@layout/layout_titlebar_base_view" />
 * 创建人：mzg
 * 创建时间：2016/11/24 14:36
 * 修改人：mzg
 * 修改时间：2016/11/24 14:36
 * 修改备注：
 */

public abstract class BaseTitleActivity extends BaseCustomSwipeRefreshActivity {
    protected TitleBarView titleBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle();
        initTitleView();
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        titleBarView = (TitleBarView) findViewById(R.id.view_title_background);
    }

    /**
     * 初始化抬头相关信息
     */
    protected abstract void initTitleView();
}
