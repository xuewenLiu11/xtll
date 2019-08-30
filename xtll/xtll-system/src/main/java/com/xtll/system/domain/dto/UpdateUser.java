package com.xtll.system.domain.dto;

import com.xtll.common.core.domain.BaseEntity;
import com.xtll.system.domain.vo.AreasPacForAgtAndBuss;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class UpdateUser  {
    private Integer id;

    private String name;

    private String loginName;

    private String password;

    private String telephone;

    private String filePath;

    private Short status;

    private Date createTime;

    private Date updateTime;

    private String createby;

    private String remark;

    private String userCardid;

    private String certificateName;

    private Integer userId;

    private String cardidName;
    //装区域的代号
    private String[] areasNum;
    private String number;
    //---增加的 封装区域
    private List<AreasPacForAgtAndBuss> areas;

    private String type;


}
