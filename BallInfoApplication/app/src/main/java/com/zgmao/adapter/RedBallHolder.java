package com.zgmao.adapter;

import android.view.View;
import android.widget.TextView;

import com.maf.adapter.BaseRecycleViewHolder;

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

public class RedBallHolder extends BaseRecycleViewHolder {
    TextView textBallNumber;

    public RedBallHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView() {
        textBallNumber = (TextView) itemView.findViewById(R.id.text_red_ball_number);
    }
}
