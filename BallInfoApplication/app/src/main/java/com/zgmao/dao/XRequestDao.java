package com.zgmao.dao;

import android.content.Context;

import com.maf.db.DbHelper;
import com.maf.db_demo.TableDemo;
import com.maf.utils.LogUtils;
import com.maf.utils.StringUtils;
import com.zgmao.application.BallApplication;
import com.zgmao.bean.XRequest;

import java.util.List;


/**
 * 项目名称：Ytb_Android
 * 类描述：
 * 创建人：mzg
 * 创建时间：2016/11/14 14:27
 * 修改人：mzg
 * 修改时间：2016/11/14 14:27
 * 修改备注：
 */

public class XRequestDao {
    // 数据库操作类
    private DbHelper db;
    private BallApplication ap;
    private String tableName = "X_REQUEST";

    /**
     * 带context的构造函数
     *
     * @param context
     */
    public XRequestDao(Context context) {
        if (context == null) {
            ap = (BallApplication) context.getApplicationContext();
        } else {
            ap = (BallApplication) context.getApplicationContext();
        }
        db = ap.db;
        // 检查表是否存在，不存在则创建
        db.checkOrCreateTable(XRequest.class);
    }

    /**
     * 插入或者更新函数
     *
     * @param vo
     * @return 主键
     */
    public String insertOrUpdate(XRequest vo) {
        XRequest exitVo = getRequest(vo.getAction(), vo.getBody());
        if (null == exitVo) {
            // 如果已经存在的数据为空，新的数据，插入
            db.save(vo);
            // 根据demo里面定义的表名，得到最新的一个主键
            Long newId = db.getLastInsertId(tableName);
            vo.setId(newId);
            LogUtils.d("插入新的请求缓存" + vo.toString());
        } else {
            // 已经存在的数据不为空，根据主键更新
            // 此时要求根据主键要在表中能找到数据，并更新
            exitVo.setUrl(vo.getUrl());
            exitVo.setAction(vo.getAction());
            exitVo.setBody(vo.getBody());
            exitVo.setResult(vo.getResult());
            db.update(exitVo);
            LogUtils.d("更新旧的请求缓存" + exitVo.toString());
        }
        return String.valueOf(vo.getId());
    }

    /**
     * 根据条件查找保存的网络请求
     *
     * @param action 请求方法
     * @param body   请求参数
     * @return 结果
     */
    public XRequest getRequest(String action, String body) {
        if (StringUtils.isEmpty(action)) {
            return null;
        }
        XRequest exitVo;
        if (StringUtils.isEmpty(body)) {
            StringBuffer sql = new StringBuffer();
            sql.append(":action = ? ");
            exitVo = db.queryFirst(XRequest.class, sql.toString(), action);
        } else {
            StringBuffer sql = new StringBuffer();
            sql.append("action = ? and :body = ?");
            exitVo = db.queryFirst(XRequest.class, sql.toString(), action, body);
        }
        return exitVo;
    }

    /**
     * 得到所有缓存
     *
     * @return
     */
    public List<XRequest> getAllRequest() {
        return db.queryList(XRequest.class, "", "");
    }

    /**
     * 超过48h的数据删除
     *
     * @param vo vo
     */
    public boolean deleteByTime(XRequest vo) {
        Boolean result = false;
        if (vo == null) {
            return result;
        }
        if (vo.getId() == null) {
            return result;
        }
        if (vo.getId() == null) {
            return result;
        }
        try {
            String sql = "delete from " + tableName + " where id = " + vo.getId();
            LogUtils.d("sql:" + sql);
            db.getDb().execSQL(sql);
            result = true;
        } catch (Exception e) {
            return result;
        }
        return result;
    }
}
