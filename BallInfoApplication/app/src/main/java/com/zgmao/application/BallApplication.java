package com.zgmao.application;

import com.maf.application.BaseApplication;
import com.zgmao.utils.RequestUtils;

/**
 * 项目名称：BallInfoApplication
 * 类描述：
 * 创建人：mzg
 * 创建时间：2016/11/22 19:10
 * 修改人：mzg
 * 修改时间：2016/11/22 19:10
 * 修改备注：
 */

public class BallApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestUtils.initUrl();
    }
}
