package com.zgmao.utils;

import com.maf.net.XAPIServiceListener;
import com.maf.net.XBaseAPIUtils;

/**
 * 项目名称：BallInfoApplication
 * 类描述：
 * 创建人：mzg
 * 创建时间：2016/11/22 19:16
 * 修改人：mzg
 * 修改时间：2016/11/22 19:16
 * 修改备注：
 */

public class RequestUtils {
    public static String url = "http://192.168.1.178:9001";
    public static String action_info = "/api/ball/getBall";

    /**
     * 请求双色球信息
     *
     * @param listener 监听器
     */
    public static void getBallMsg(XAPIServiceListener listener) {
        XBaseAPIUtils.baseGet(url, action_info, null, null, listener);
    }
}
