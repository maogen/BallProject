package com.zgmao.activity;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maf.net.XAPIServiceListener;
import com.maf.utils.BaseToast;
import com.maf.utils.DialogUtil;
import com.maf.utils.LogUtils;
import com.maf.utils.StringUtils;
import com.zgmao.bean.Ball;
import com.zgmao.listener.MyEditTextWatcher;
import com.zgmao.utils.RequestUtils;
import com.zgmao.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：查看是否中奖界面
 * 创建人：mzg
 * 创建时间：2016/12/5 18:23
 * 修改人：mzg
 * 修改时间：2016/12/5 18:23
 * 修改备注：
 */

public class NumberWinActivity extends BaseTitleActivity {
    private int[] editIds = {R.id.edit_input_1, R.id.edit_input_2,
            R.id.edit_input_3, R.id.edit_input_4,
            R.id.edit_input_5, R.id.edit_input_6,
            R.id.edit_input_blue};// 输入框id
    private EditText[] editTexts = new EditText[editIds.length];// 输入框

    private Button btnSure;// 确定按钮
    private TextView textMsg;// 结果显示
    private TextView textReInput;// 重新输入

    @Override
    protected void initTitleView() {
        titleBarView.setTitle("是否中奖");
    }

    @Override
    public int getSwipeRefreshId() {
        return 0;
    }

    @Override
    public void refreshing() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_number_win;
    }

    @Override
    protected void initView() {
        for (int i = 0; i < editIds.length; i++) {
            editTexts[i] = (EditText) findViewById(editIds[i]);
        }

        btnSure = (Button) findViewById(R.id.btn_analysis_win);
        textMsg = (TextView) findViewById(R.id.text_analysis_msg);
        textReInput = (TextView) findViewById(R.id.text_re_input);
        // 添加下划线
        textReInput.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initEvent() {
        btnSure.setOnClickListener(this);
        textReInput.setOnClickListener(this);
    }

    @Override
    protected void initValue() {
        for (int i = 0; i < editIds.length; i++) {
            // 为输入框添加监听器，当输入满两个数字时，跳到下一个
            MyEditTextWatcher textWatcher;
            if (i < editIds.length - 1) {
                textWatcher = new MyEditTextWatcher(this, editTexts[i], editTexts[i + 1]);
            } else {
                textWatcher = new MyEditTextWatcher(this, editTexts[i], null);
            }
            editTexts[i].addTextChangedListener(textWatcher);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_re_input:
                // 重新输入
                for (int i = 0; i < editTexts.length; i++) {
                    editTexts[i].setText("");
                }
                editTexts[0].requestFocus();
                ViewUtils.showInputMethodManager(editTexts[0]);

                textMsg.setText("");
                break;
            case R.id.btn_analysis_win:
                // 获取红色号码
                List<Integer> redList = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    String content = editTexts[i].getText().toString();
                    if (StringUtils.isEmpty(content)) {
                        BaseToast.makeTextShort("红色号码不能为空");
                        return;
                    }
                    int number = Integer.valueOf(content);
                    if (number <= 0 || number > 33) {
                        BaseToast.makeTextShort("红色区域请输入01-33之间的数字");
                        return;
                    }
                    for (int j = 0; j < redList.size(); j++) {
                        if (number == redList.get(j)) {
                            BaseToast.makeTextShort("红色号码不能重复");
                            return;
                        }
                    }
                    redList.add(number);
                }
                //  获得蓝号
                String content = editTexts[6].getText().toString();
                if (StringUtils.isEmpty(content)) {
                    BaseToast.makeTextShort("蓝色号码不能为空");
                    return;
                }
                int blueNumber = Integer.valueOf(content);
                if (blueNumber <= 0 || blueNumber > 16) {
                    BaseToast.makeTextShort("蓝色区域请输入01-16之间的数字");
                    return;
                }
                // 构造数据
                Ball ball = new Ball();
                ball.setRedNumber(redList);
                ball.setBlueNumber(blueNumber);
                request(ball);
                break;
            default:
                break;
        }
    }

    /**
     * 请求判断
     *
     * @param ball
     */
    private void request(Ball ball) {
        textMsg.setText("");
        DialogUtil.showProgressDialog(this);
        RequestUtils.getAnalysisWin(new XAPIServiceListener() {
            @Override
            public void onSuccess(String result) {
                LogUtils.d(result);
                textMsg.setText(result);
            }

            @Override
            public void onError(String result) {
                BaseToast.makeTextShort("请求失败");
            }

            @Override
            public void onFinished() {
                DialogUtil.dismissDialog();
            }
        }, ball);
    }
}
