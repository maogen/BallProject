package com.zgmao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.maf.adapter.BaseRecycleViewHolder;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：
 * 创建人：mzg
 * 创建时间：2016/11/29 16:30
 * 修改人：mzg
 * 修改时间：2016/11/29 16:30
 * 修改备注：
 */

public class HistoryBallHolder extends BaseRecycleViewHolder {
    TextView textNumber;// 期号
    TextView textDate;// 日期
    RecyclerView recyclerViewRedBall;// 红球
    TextView textBlueBall;// 篮球
    TextView textWinInfo;

    public HistoryBallHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView() {
        textNumber = (TextView) itemView.findViewById(R.id.text_ball_number);
        textDate = (TextView) itemView.findViewById(R.id.text_ball_date);
        recyclerViewRedBall = (RecyclerView) itemView.findViewById(R.id.recycle_red_ball);
        textBlueBall = (TextView) itemView.findViewById(R.id.text_blue_ball_number);
        textWinInfo = (TextView) itemView.findViewById(R.id.text_win_info);
    }
}
