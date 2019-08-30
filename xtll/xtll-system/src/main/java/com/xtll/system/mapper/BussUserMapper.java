package com.xtll.system.mapper;

import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.BussUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BussUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BussUser record);

    /**
     * 插入
     * @param record
     * @return
     */
    int insertSelective(BussUser record);

    BussUser selectByPrimaryKey(Integer id);



    int updateByPrimaryKey(BussUser record);

    @Select("select * from Buss_user")
    List<BussUser> selectList();


    /**
     * 查找管理员省份的人
     * @param province
     * @return
     */
    @Select("select bu.* from buss_user bu LEFT JOIN xt_user u on bu.buss_province=u.user_province where u.user_province=#{province};")
    List<BussUser> selectListByProv(String province);

    /**
     * 根据用户名删除
     * @param loginName
     * @return
     */
    @Delete("delete from buss_user where login_name=#{loginName}")
    int deleteByLoginName(String loginName);


    /**
     * 修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(BussUser record);


    /**
     * 根据地区地址来查询商户
     * @param areaId
     * @return
     */


    @Select("select * from buss_user where area_id=#{areaId}")
    List<BussUser> selectListByAreas(String areaId);


    List<BussUser> selectListByStatus(int[] statu);

    /**
     * 查看当前账户一审未通过的
     * @param id
     * @return
     */
    @Select("select bu.* from xt_user xu LEFT JOIN buss_user bu on xu.id=bu.user_id where xu.id=#{id} and bu.status=\"4\"")
    List<BussUser> selectFailPath1(Integer id);
}