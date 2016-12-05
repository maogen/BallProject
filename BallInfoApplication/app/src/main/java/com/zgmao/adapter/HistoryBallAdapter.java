package com.zgmao.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.maf.adapter.BaseRecycleAdapter;
import com.maf.utils.NumberUtils;
import com.zgmao.bean.Ball;

import java.util.List;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：
 * 创建人：mzg
 * 创建时间：2016/11/29 16:35
 * 修改人：mzg
 * 修改时间：2016/11/29 16:35
 * 修改备注：
 */

public class HistoryBallAdapter extends BaseRecycleAdapter<Ball, HistoryBallHolder> {
    public HistoryBallAdapter(Context context, List<Ball> list) {
        super(context, list);
    }

    @Override
    protected int getResourceId() {
        return R.layout.item_ball_history;
    }

    @Override
    protected HistoryBallHolder getViewHolder(View view) {
        HistoryBallHolder ballHolder = new HistoryBallHolder(view);
        return ballHolder;
    }

    @Override
    public void onBindViewHolder(HistoryBallHolder holder, int position) {
        Ball item = list.get(position);
        holder.textNumber.setText(position + "：第" + item.getBallNumber() + "期  ");
        holder.textDate.setText(item.getBallDate());
        // 设置红球
        RedBallAdapter adapter = new RedBallAdapter(context, item.getRedNumber());
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerViewRedBall.setLayoutManager(manager);
        holder.recyclerViewRedBall.setAdapter(adapter);
        holder.recyclerViewRedBall.getAdapter().notifyDataSetChanged();
        // 设置篮球
        holder.textBlueBall.setText(NumberUtils.intTo2Dec(item.getBlueNumber()));
        // 设置获奖信息
        holder.textWinInfo.setText(item.getWinInfo());
    }
}
