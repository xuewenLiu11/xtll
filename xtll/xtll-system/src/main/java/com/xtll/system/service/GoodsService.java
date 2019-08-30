package com.xtll.system.service;

import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.Goods;
import com.xtll.system.domain.GoodsAdd;

import java.util.List;

public interface GoodsService {


    /**
     * 查询列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageUtils<Goods> selectList(Integer pageNum, Integer pageSize);


    int updateByPrimaryKeySelective(Goods record);



    int insert(Goods record);


    int deleteByPrimaryKey(Integer id);



}
