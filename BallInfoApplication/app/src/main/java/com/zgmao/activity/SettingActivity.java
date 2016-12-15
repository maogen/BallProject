package com.zgmao.activity;

import android.view.View;
import android.widget.EditText;

import com.maf.utils.BaseToast;
import com.maf.utils.SPUtils;
import com.maf.utils.StringUtils;
import com.zgmao.utils.RequestUtils;

import zgmao.com.ballinfo.R;

/**
 * 项目名称：BallInfoApplication
 * 类描述：设置界面
 * 创建人：mzg
 * 创建时间：2016/12/15 15:24
 * 修改人：mzg
 * 修改时间：2016/12/15 15:24
 * 修改备注：
 */

public class SettingActivity extends com.maf.activity.BaseTitleActivity {
    private EditText editIp;
    private EditText editPort;

    @Override
    protected void initTitleView() {
        titleBarView.setTitle("设置");
        titleBarView.setTitleRight("确定", this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        editIp = (EditText) findViewById(R.id.edit_input_ip);
        editPort = (EditText) findViewById(R.id.edit_input_port);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initValue() {
        editIp.setText((String) SPUtils.get(this, RequestUtils.key_ip, RequestUtils.url_ip));
        editPort.setText((String) SPUtils.get(this, RequestUtils.key_port, RequestUtils.url_port));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_title_right:
                String ip = editIp.getText().toString();
                if (StringUtils.isEmpty(ip)) {
                    BaseToast.makeTextShort("ip地址不能为空");
                    return;
                }
                String port = editPort.getText().toString();
                if (StringUtils.isEmpty(port)) {
                    BaseToast.makeTextShort("端口不能为空");
                    return;
                }
                SPUtils.put(this, RequestUtils.key_ip, ip);
                SPUtils.put(this, RequestUtils.key_port, port);
                // 保存地址之后，重新设置一下网络请求的地址
                RequestUtils.initUrl();
                finish();
                break;
            default:
                break;
        }
    }

}
