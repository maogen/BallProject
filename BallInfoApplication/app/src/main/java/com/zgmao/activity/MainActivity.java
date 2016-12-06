package com.zgmao.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.maf.git.GsonUtils;
import com.maf.net.XAPIServiceListener;
import com.maf.popupwindow.BaseListPopup;
import com.maf.utils.BaseToast;
import com.maf.utils.DialogUtil;
import com.maf.utils.LogUtils;
import com.maf.utils.NumberUtils;
import com.zgmao.adapter.RedBallAdapter;
import com.zgmao.bean.AnalysisResult;
import com.zgmao.bean.Ball;
import com.zgmao.bean.NumberRate;
import com.zgmao.bean.RecommendBall;
import com.zgmao.bean.XRequest;
import com.zgmao.dao.XRequestDao;
import com.zgmao.utils.RequestUtils;

import java.util.List;

import zgmao.com.ballinfo.R;

public class MainActivity extends BaseTitleActivity {
    private LinearLayout viewInfo;// 初始化时，隐藏控件
    private TextView textBallDate;// 显示日期
    private RecyclerView recyclerView;// 显示红球
    private TextView textBlueBall;// 显示蓝球
    private TextView textWinInfo;
    private Button btnNext;// 下期预测
    private TextView textNextMsg;// 下期预测号码
    private Ball ball;

    // 缓存数据库操作
    private XRequestDao xRequestDao;

    private BaseListPopup listPopup;

    @Override
    protected void initTitleView() {
        titleBarView.setTitle("第xxxxxxx期开奖结果");
        titleBarView.setOnMenuClick(this);
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
        viewInfo = (LinearLayout) findViewById(R.id.view_ball_info);
        textBallDate = (TextView) findViewById(R.id.text_ball_date);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_red_ball);
        textBlueBall = (TextView) findViewById(R.id.text_blue_ball_number);
        textWinInfo = (TextView) findViewById(R.id.text_win_info);
        btnNext = (Button) findViewById(R.id.btn_next_calculate);
        textNextMsg = (TextView) findViewById(R.id.text_analysis_number);

        xRequestDao = new XRequestDao(this);
        listPopup = new BaseListPopup(this);
        String[] menus = new String[]{"查看历史", "是否中奖"};
        listPopup.setMenu(menus, null, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    // 进入查看历史界面
                    startActivity(HistoryActivity.class);
                } else if (i == 1) {
                    //查看获奖情况
                    startActivity(NumberWinActivity.class);
                }
                listPopup.dismiss();
            }
        });
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
                requestAnalysis();
                break;
            case R.id.text_title_right:
                // 进入查看历史界面
                startActivity(HistoryActivity.class);
                break;
            case R.id.image_title_menu:
                // 显示菜单
                listPopup.showBottomByView(v);
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
            viewInfo.setVisibility(View.GONE);
            return;
        }
        viewInfo.setVisibility(View.VISIBLE);
        // 设置标题、设置期号
        titleBarView.setTitle("第" + ball.getBallNumber() + "期");
        //设置日期
        textBallDate.setText(ball.getBallDate());
        // 设置红球
        RedBallAdapter adapter = new RedBallAdapter(this, ball.getRedNumber());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        // 设置篮球
        textBlueBall.setText(NumberUtils.intTo2Dec(ball.getBlueNumber()));
        // 设置获奖信息
        textWinInfo.setText(ball.getWinInfo());
    }

    /**
     * 根据分析结果，显示界面
     *
     * @param analysisResult
     */
    private void setAnalysisView(AnalysisResult analysisResult) {
        if (analysisResult == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        // 获取红球
        RecommendBall red = analysisResult.getRedBall();
        if (red != null) {
            List<NumberRate> rates = red.getMustNumberList();
            if (rates == null || rates.size() == 0) {
                sb.append("无强烈推荐红球号码\n");
            } else {
                sb.append("强烈推荐红球号码：\n");
                for (NumberRate item : rates) {
                    sb.append(NumberUtils.intTo2Dec(item.getNumber()) + "  ");
                }
                sb.append("\n");
            }
            List<NumberRate> rates2 = red.getNeedNumberList();
            if (rates2 != null && rates2.size() > 0) {
                sb.append("推荐红球号码：\n");
                for (NumberRate item : rates2) {
                    sb.append(NumberUtils.intTo2Dec(item.getNumber()) + "  ");
                }
                sb.append("\n");

            }
        }
        // 获取蓝球
        RecommendBall blue = analysisResult.getBlueBall();
        if (blue != null) {
            List<NumberRate> rates3 = blue.getMustNumberList();
            if (rates3 == null || rates3.size() == 0) {
                sb.append("无强烈推荐蓝球号码\n");
            } else {
                sb.append("强烈推荐蓝球号码：\n");
                for (NumberRate item : rates3) {
                    sb.append(NumberUtils.intTo2Dec(item.getNumber()) + "  ");
                }
                sb.append("\n");
            }
            List<NumberRate> rates4 = blue.getNeedNumberList();
            if (rates4 != null && rates4.size() > 0) {
                sb.append("推荐蓝球号码：\n");
                for (NumberRate item : rates4) {
                    sb.append(NumberUtils.intTo2Dec(item.getNumber()) + "  ");
                }
                sb.append("\n");
            }
        }
        textNextMsg.setText(sb.toString());
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
                // 请求成功，插入缓存
                XRequest xRequest = new XRequest();
                xRequest.setUrl(RequestUtils.url);
                xRequest.setAction(RequestUtils.action_info);
                xRequest.setBody(null);
                xRequest.setResult(result);
                xRequestDao.insertOrUpdate(xRequest);
            }

            @Override
            public void onError(String result) {
                LogUtils.d(result);
                BaseToast.makeTextShort("请求数据失败");
                // 获取数据库
                XRequest xRequest = xRequestDao.getRequest(RequestUtils.url, RequestUtils.action_info, null);
                if (xRequest != null) {
                    ball = GsonUtils.stringToGson(xRequest.getResult(), new TypeToken<Ball>() {
                    });
                    setBallView();
                }
            }

            @Override
            public void onFinished() {
                finishRefresh();
            }
        });
    }

    /**
     * 请求分析结果信息
     */
    private void requestAnalysis() {
        DialogUtil.showProgressDialog(this);
        RequestUtils.getAnalysis(new XAPIServiceListener() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d(result);
                AnalysisResult analysisResult = GsonUtils.stringToGson(result, new TypeToken<AnalysisResult>() {
                });
                setAnalysisView(analysisResult);
                // 请求成功，插入缓存
                XRequest xRequest = new XRequest();
                xRequest.setUrl(RequestUtils.url);
                xRequest.setAction(RequestUtils.action_analysis);
                xRequest.setBody(null);
                xRequest.setResult(result);
                xRequestDao.insertOrUpdate(xRequest);
            }

            @Override
            public void onError(String result) {
                LogUtils.d(result);
                BaseToast.makeTextShort("请求数据失败");
                // 获取数据库
                XRequest xRequest = xRequestDao.getRequest(RequestUtils.url, RequestUtils.action_analysis, null);
                if (xRequest != null) {
                    AnalysisResult analysisResult = GsonUtils.stringToGson(xRequest.getResult(), new TypeToken<AnalysisResult>() {
                    });
                    setAnalysisView(analysisResult);
                }
            }

            @Override
            public void onFinished() {
                DialogUtil.dismissDialog();
            }
        });
    }
}
