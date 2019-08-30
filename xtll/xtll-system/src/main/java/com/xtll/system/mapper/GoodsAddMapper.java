package com.xtll.system.mapper;

import com.xtll.system.domain.GoodsAdd;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsAddMapper {
    int deleteByPrimaryKey(String id);

    int insert(GoodsAdd record);

    int insertSelective(GoodsAdd record);

    GoodsAdd selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GoodsAdd record);

    int updateByPrimaryKey(GoodsAdd record);

    @Select("select * from goods_add")
    List<GoodsAdd> selectList();


    /**
     * 商品审核
     * @param statu
     * @param typeFirst
     * @param typeSecond
     * @param typeThird
     * @return
     */
    List<GoodsAdd> selectListByStatus(@Param("statu") int[] statu, @Param("typeFirst") String typeFirst, @Param("typeSecond") String typeSecond, @Param("typeThird") String typeThird);


    @Select("select * from goods_add where status=\"1\" and type_third=#{typeThird} and distribution like \"%${distribution}%\";")
    List<GoodsAdd> selectListSameDist(@Param("typeThird")String typeThird,@Param("distribution")String distribution);
}