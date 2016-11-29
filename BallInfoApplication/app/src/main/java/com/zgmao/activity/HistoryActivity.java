package com.zgmao.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.maf.git.GsonUtils;
import com.maf.net.XAPIServiceListener;
import com.maf.utils.BaseToast;
import com.maf.utils.LogUtils;
import com.zgmao.adapter.HistoryBallAdapter;
import com.zgmao.bean.Ball;
import com.zgmao.utils.RequestUtils;


import java.util.ArrayList;
import java.util.List;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：查看历史记录
 * 创建人：mzg
 * 创建时间：2016/11/29 11:57
 * 修改人：mzg
 * 修改时间：2016/11/29 11:57
 * 修改备注：
 */

public class HistoryActivity extends BaseTitleActivity {
    private RecyclerView recyclerHistory;
    private HistoryBallAdapter adapter;
    private List<Ball> ballList;


    @Override
    protected void initTitleView() {
        titleBarView.setTitle("历史号码");
    }

    @Override
    public int getSwipeRefreshId() {
        return R.id.swipe_container;
    }

    @Override
    public void refreshing() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_hisory;
    }

    @Override
    protected void initView() {
        recyclerHistory = (RecyclerView) findViewById(R.id.recycle_history);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initValue() {
        ballList = new ArrayList<>();
        adapter = new HistoryBallAdapter(this, ballList);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerHistory.setAdapter(adapter);
        request();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 请求数据
     */
    private void request() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.autoRefresh();
        }
        RequestUtils.getHistory(new XAPIServiceListener() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d(result);
                List<Ball> balls = GsonUtils.stringToGson(result, new TypeToken<List<Ball>>() {
                });
                if (balls != null && balls.size() > 0) {
                    ballList.clear();
                    ballList.addAll(balls);
                    recyclerHistory.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String result) {
                LogUtils.d(result);
                BaseToast.makeTextShort("请求数据失败");
            }

            @Override
            public void onFinished() {
                finishRefresh();
            }
        });
    }
}
