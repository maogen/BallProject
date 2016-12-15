package com.zgmao.utils;

import com.maf.net.XAPIServiceListener;
import com.maf.net.XBaseAPIUtils;
import com.maf.utils.SPUtils;
import com.maf.utils.StringUtils;
import com.zgmao.application.BallApplication;
import com.zgmao.bean.Ball;

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

    public static String key_ip = "url_ip";
    public static String url_ip = "192.168.1.180";
    public static String key_port = "url_port";
    public static String url_port = "9001";

    public static String url = "http://" + url_ip + ":" + url_port;
    public static String action_info = "/api/ball/getBall";// 获取最近一期号码
    public static String action_history = "/api/ball/getHistoryByPage";// 获取历史号码
    public static String action_analysis = "/api/ball/analysis";// 获取推荐号码
    public static String action_analisis_win = "/api/ball/analyseWin";// 判断是否中奖

    /**
     * 设置访问的地址
     *
     * @param url_ip   ip
     * @param url_port 端口
     */
    public static void setUrl(String url_ip, String url_port) {
        url = "http://" + url_ip + ":" + url_port;
    }

    /**
     * 初始化url
     */
    public static void initUrl() {
        String url_ip = (String) SPUtils.get(BallApplication._application, RequestUtils.key_ip, RequestUtils.url_ip);
        String url_port = (String) SPUtils.get(BallApplication._application, RequestUtils.key_port, RequestUtils.url_port);
        if (StringUtils.isNotEmpty(url_ip)) {
            setUrl(url_ip, url_port);
        }
    }

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

    /**
     * 判断是否中奖
     *
     * @param listener 监听器
     * @param ball
     */
    public static void getAnalysisWin(XAPIServiceListener listener, Ball ball) {
        XBaseAPIUtils.postObject(url, action_analisis_win, ball, null, listener);
    }
}
