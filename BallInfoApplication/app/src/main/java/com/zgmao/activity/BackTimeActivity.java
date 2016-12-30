package com.zgmao.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.maf.activity.BaseTitleActivity;
import com.maf.utils.BaseToast;
import com.maf.utils.DateUtils;
import com.maf.utils.LogUtils;
import com.maf.utils.SPUtils;
import com.maf.utils.StringUtils;
import com.zgmao.utils.ViewUtils;

import java.util.Date;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：倒计时
 * 创建人：mzg
 * 创建时间：2016/12/30 8:54
 * 修改人：mzg
 * 修改时间：2016/12/30 8:54
 * 修改备注：
 */

public class BackTimeActivity extends BaseTitleActivity {
    // 开始计时日期
    private String startTime = "2016-12-30";
    // 日期格式
    private String dateFormat = "yyyy-MM-dd";
    //最大能量数
    private final int MAX_ENERGY = 17900;
    // 开始计时的时候的能量数
    private int startEnergy = 5445;

    // 显示倒计时文本
    private TextView textBackTime;
    // 当前能量
    private EditText editNowEnergy;
    // 编辑或者确定当前能量
    private TextView textEditEnergy;

    @Override
    protected void initTitleView() {
        titleBarView.setTitle("倒计时");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_back_time;
    }

    @Override
    protected void initView() {
        textBackTime = (TextView) findViewById(R.id.text_back_time);
        editNowEnergy = (EditText) findViewById(R.id.edit_now_energy);
        textEditEnergy = (TextView) findViewById(R.id.text_edit_energy);
    }

    @Override
    protected void initEvent() {
        textEditEnergy.setOnClickListener(this);
    }

    @Override
    protected void initValue() {
        // 计算倒计时
        int spaceTime = getTimeSpace();
        LogUtils.d("距离开始日期天数：" + spaceTime);
        setBackDay(spaceTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_edit_energy:
                if (editNowEnergy.isEnabled()) {
                    // 编辑框可编辑
                    String editContent = editNowEnergy.getText().toString();
                    if (StringUtils.isEmpty(editContent)) {
                        BaseToast.makeTextShort("当前能量不能为空");
                        ViewUtils.showInputMethodManager(editNowEnergy);
                        return;
                    }
                    int nowEnergy = Integer.valueOf(editContent);
                    SPUtils.put(this, "nowEnergy", Integer.valueOf(nowEnergy));
                    editNowEnergy.setEnabled(false);
                    textEditEnergy.setText("编辑");
                    setBackDay(getTimeSpace());
                } else {
                    // 编辑框不可编辑
                    textEditEnergy.setText("确定");
                    editNowEnergy.setEnabled(true);
                    ViewUtils.showInputMethodManager(editNowEnergy);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 计算今天距离还是日期的天数
     *
     * @return
     */
    private int getTimeSpace() {
        Date startDate = DateUtils.getDateByFormatDate(startTime, dateFormat);
        Date nowDate = new Date();
        long spaceTime = nowDate.getTime() - startDate.getTime();
        return (int) spaceTime / 1000 / 60 / 60 / 24;
    }

    /**
     * 设置倒计时文本
     *
     * @param spaceTime
     */
    private void setBackDay(int spaceTime) {
        // 当前能量没保存，计算
        int dayEnergy = 30;// 每天浇水的能量点
        // 得到当前能量
        int nowEnergy = (int) SPUtils.get(this, "nowEnergy", Integer.valueOf(0));
        if (nowEnergy == 0) {
            // 现在的能量点
            nowEnergy = startEnergy + dayEnergy * spaceTime;
            // 保存
            SPUtils.put(this, "nowEnergy", Integer.valueOf(nowEnergy));
        }
        //还差能量点
        int shortEnergy = MAX_ENERGY - nowEnergy;
        double shortDay = (double) shortEnergy / dayEnergy;
        int backDay = (int) Math.ceil(shortDay);
        LogUtils.d("倒计时" + backDay + "天");
        textBackTime.setText(backDay + "");

        // 初始化当前能量
        editNowEnergy.setText(nowEnergy + "");
        editNowEnergy.setEnabled(false);
    }
}
