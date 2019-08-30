package com.xtll.system.mapper;

import com.xtll.system.domain.Goods;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    @Select("select * from goods")
    List<Goods> selectList();


}