package com.xtll.system.mapper;

import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.Areas;
import com.xtll.system.domain.vo.AgtCardId;
import com.xtll.system.domain.vo.AreasPExamGoods;
import com.xtll.system.domain.vo.AreasPac;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AreasMapper {
    int deleteByPrimaryKey(String id);

    int insert(Areas record);

    int insertSelective(Areas record);

    Areas selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Areas record);

    int updateByPrimaryKey(Areas record);


    /**
     * 根据id查询代理商的标识
     * @param id
     * @return
     */
    @Select("SELECT agt_cardId from areas where ID=#{id}")
    AgtCardId selectCardIdById(String id);

    /**
     * 添加代理商
     * @param areaId
     * @return
     */

    @Update("update areas set agt_cardId =#{cardId} where ID =#{areaId}")
    int insertCardId(@Param("cardId")String cardId,@Param("areaId")String areaId);


    /**
     * 添加代理商前判断这个区域有没有代理商
     */

    @Select("SELECT agt_cardId from areas where ID=#{areaId}")
    String checkHasAgtOrNot(String areaId);


    /**
     * 根据代号查询区域省市区
     */
    @Select("select province,city,district from areas where ID=#{areaId}")
    AreasPac selectAreaName(String areaId);


    @Select("select province,city,district from areas where ID=#{areaId}")
    AreasPExamGoods selectAreaNameForExamGoods(String areaId);


    /**
     * 根据市查询
     * @param city
     * @return
     */
    @Select("select * from areas where City=#{city}")
    List<Areas> selectByCity(String city);

    /**
     * 根据省查询区域
     * @param province
     * @return
     */
    @Select("select * from areas where Province=#{province}")
    List<Areas> selectByProvince(String province);


    @Select("select * from areas where agt_cardId=#{agtCardId}")
    List<Areas> selectByCardId(String agtCardId);


    @Select("select * from areas")
    List<Areas> selectList();
}


