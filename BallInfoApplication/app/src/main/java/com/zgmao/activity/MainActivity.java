package com.zgmao.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.maf.git.GsonUtils;
import com.maf.net.XAPIServiceListener;
import com.maf.utils.BaseToast;
import com.maf.utils.LogUtils;
import com.maf.utils.NumberUtils;
import com.zgmao.adapter.RedBallAdapter;
import com.zgmao.bean.Ball;
import com.zgmao.utils.RequestUtils;

import zgmao.com.ballinfo.R;

public class MainActivity extends BaseTitleActivity {
    private TextView textBallDate;// 显示日期
    private RecyclerView recyclerView;// 显示红球
    private TextView textBlueBall;// 显示蓝球
    private Button btnNext;// 下期预测
    private Ball ball;

    @Override
    protected void initTitleView() {
        titleBarView.setTitle("第xxxxxxx期开奖结果");
    }

    @Override
    public int getSwipeRefreshId() {
        return R.id.swipe_container;
    }

    @Override
    public void refreshing() {
        request();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        textBallDate = (TextView) findViewById(R.id.text_ball_date);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_red_ball);
        textBlueBall = (TextView) findViewById(R.id.text_blue_ball_number);
        btnNext = (Button) findViewById(R.id.btn_next_calculate);
    }

    @Override
    protected void initEvent() {
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void initValue() {
        setBallView();
        request();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_calculate:
                // 下期预测
                break;
            default:
                break;
        }
    }

    /**
     * 设置数据
     */
    private void setBallView() {
        if (ball == null) {
            textBlueBall.setVisibility(View.GONE);
            textBallDate.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
            return;
        }
        titleBarView.setTitle(ball.getBallNumber());
        textBallDate.setText(ball.getBallDate());
        textBallDate.setVisibility(View.VISIBLE);
        // 设置红球
        RedBallAdapter adapter = new RedBallAdapter(this, ball.getRedNumber());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        // 设置篮球
        textBlueBall.setVisibility(View.VISIBLE);
        textBlueBall.setText(NumberUtils.intTo2Dec(ball.getBlueNumber()));
        // 显示下期按钮
        btnNext.setVisibility(View.VISIBLE);
    }

    /**
     * 请求数据
     */
    private void request() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.autoRefresh();
        }
        RequestUtils.getBallMsg(new XAPIServiceListener() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d(result);
                ball = GsonUtils.stringToGson(result, new TypeToken<Ball>() {
                });
                setBallView();
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
