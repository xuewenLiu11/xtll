package com.xtll.system.service;

import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.GoodsAdd;
import com.xtll.system.domain.vo.AreasPExamGoods;
import com.xtll.system.domain.vo.AreasPac;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsAddService {

    PageUtils<GoodsAdd> selectPageList(Integer pageNum,Integer pageSize);

    PageUtils<GoodsAdd> selectListByStatus(Integer pageNum,Integer pageSize,int[] statu,String typeFirst,String typeSecond,String typeThird);

    List<AreasPExamGoods> selectListSameDist(String typeThird, String distribution)throws Exception;

    GoodsAdd selectByPrimaryKey(String id);

}
