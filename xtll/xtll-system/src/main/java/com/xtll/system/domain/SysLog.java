package com.xtll.system.domain;

import lombok.Data;

import java.util.Date;

@Data
public class SysLog {
    private String id;

    private String title;

    private String operator;

    private String operatedobject;

    private String type;

    private String api;

    private String ip;

    private Date createTime;

    private String remark;

    public SysLog(String id, String title, String operator, String operatedobject, String type, String api, String ip, Date createTime) {
        this.id = id;
        this.title = title;
        this.operator = operator;
        this.operatedobject = operatedobject;
        this.type = type;
        this.api = api;
        this.ip = ip;
        this.createTime = createTime;
    }
}