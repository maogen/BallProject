package com.zgmao.utils;

import com.maf.net.XAPIServiceListener;
import com.maf.net.XBaseAPIUtils;

import java.util.HashMap;
import java.util.Map;

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
    public static String url = "http://192.168.1.179:9001";
    public static String action_info = "/api/ball/getBall";// 获取最近一期号码
    public static String action_history = "/api/ball/getHistoryByPage";// 获取历史号码
    public static String action_analysis = "/api/ball/analysis";// 获取推荐号码

    /**
     * 请求双色球信息
     *
     * @param listener 监听器
     */
    public static void getBallMsg(XAPIServiceListener listener) {
        XBaseAPIUtils.baseGet(url, action_info, null, null, listener);
    }

    /**
     * 请求历史信息
     *
     * @param listener 监听器
     */
    public static void getHistory(XAPIServiceListener listener, int page) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        XBaseAPIUtils.getObject(url, action_history, null, map, listener);
    }

    /**
     * 请求推荐号码
     *
     * @param listener 监听器
     */
    public static void getAnalysis(XAPIServiceListener listener) {
        XBaseAPIUtils.baseGet(url, action_analysis, null, null, listener);
    }
}
