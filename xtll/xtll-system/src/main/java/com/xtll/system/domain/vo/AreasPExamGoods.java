package com.xtll.system.domain.vo;


import com.xtll.system.domain.GoodsAdd;
import lombok.Data;

import java.util.List;

@Data
public class AreasPExamGoods {

    //商品审核需要查询某个区域的某商品的总量
    private Integer total;

    private String province;

    private String city;

    private String district;

    private List<GoodsAdd> rows;

}
