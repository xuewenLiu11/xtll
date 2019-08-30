package com.xtll.system.domain;

import com.xtll.common.core.domain.BaseEntity;
import com.xtll.system.domain.vo.AreasPac;
import com.xtll.system.domain.vo.AreasPacForAgtAndBuss;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@Data
public class BussUser  {

    private Integer id;

    private String name;

    private String loginName;

    private String password;

    private String telephone;

    private Integer roleId;

    private Short status;

    private Date createTime;

    private Date updateTime;

    private String createby;

    private String remark;

    private String userCardid;

    private String certificateName;

    private Integer userId;

    private Integer agtId;

    private String cardidName;

    private String filePath;

    private String areaId;

    private String number;
    private  String[] areasNum;
    private  List<String> areaNums;

    //---增加的 封装区域
    private List<AreasPac> areas;
}