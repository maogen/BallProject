package com.zgmao.bean;


import com.maf.db.Column;
import com.maf.db.Entity;

/**
 * 项目名称：Ytb_Android
 * 类描述：网络请求数据保存表，当无网络时，获取上次请求的数据
 * 创建人：mzg
 * 创建时间：2016/11/14 14:19
 * 修改人：mzg
 * 修改时间：2016/11/14 14:19
 * 修改备注：
 */
@Entity(table = "X_REQUEST")
public class XRequest{
    @Column(auto = true, pk = true)
    private Long id;
    @Column
    private String url;
    @Column
    private String action;
    @Column
    private String body;
    @Column
    private String result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
