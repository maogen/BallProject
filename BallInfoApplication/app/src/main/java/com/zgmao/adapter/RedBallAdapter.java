package com.zgmao.adapter;

import android.content.Context;
import android.view.View;

import com.maf.adapter.BaseRecycleAdapter;

import java.util.List;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：
 * 创建人：mzg
 * 创建时间：2016/11/22 19:55
 * 修改人：mzg
 * 修改时间：2016/11/22 19:55
 * 修改备注：
 */

public class RedBallAdapter extends BaseRecycleAdapter<Integer, RedBallHolder> {
    public RedBallAdapter(Context context, List<Integer> list) {
        super(context, list);
    }

    @Override
    protected int getResourceId() {
        return R.layout.item_ball_text;
    }

    @Override
    protected RedBallHolder getViewHolder(View view) {
        RedBallHolder holder = new RedBallHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RedBallHolder holder, int position) {
        holder.textBallNumber.setText(String.valueOf(list.get(position)));
    }
}
