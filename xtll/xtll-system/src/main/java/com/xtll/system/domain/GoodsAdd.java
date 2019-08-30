package com.xtll.system.domain;

import com.xtll.system.domain.vo.AreasPac;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GoodsAdd {
    private String id;

    private String name;

    private String typeThird;

    private String farm;

    private String specs;

    private String seasinal;

    private Date addedDate;

    private Date offDate;

    private Double merchantPrice;

    private Double platformPrice;

    private String distribution;

    private Date addTime;

    private String status;

    private Integer bussId;

    private String typeSecond;

    private String typeFirst;



    //---增加的--他传我的
    private String[] areasNum;

    //---增加的 封装区域
    private List<AreasPac> areas;

    //---增加的  封装区域代号父级子级
    private List<String> areaNums;


}