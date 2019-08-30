package com.xtll.system.domain;

import com.xtll.common.core.domain.BaseEntity;
import com.xtll.system.domain.vo.AreasPac;
import com.xtll.system.domain.vo.AreasPacForAgtAndBuss;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Data
public class AgtUser{
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
   // private List<AreasPacForAgtAndBuss> areas;
    //---增加的 封装区域
    private List<AreasPac> areas;

    //---增加的  封装区域代号父级子级
    private List<String> areaNums;


}