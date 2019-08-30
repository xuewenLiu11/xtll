package com.xtll.system.mapper;

import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.XtUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AgtUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AgtUser record);

    /**
     * 添加代理商
     * @param record
     * @return
     */
    int insertSelective(AgtUser record);

    /**
     * 根据id查询当前用户 为了查出他的区域
     * @param id
     * @return
     */
    AgtUser selectByPrimaryKey(Integer id);
    int updateByPrimaryKey(AgtUser record);



    /**
     * 修改商户
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AgtUser record);

    /**
     * 根据status来查询
     * @param statu
     * @return
     */
    List<AgtUser> selectListByStatus(int[] statu);



    /**
     * 查询所有代理商
     * @return
     */

    @Select("select * from agt_user")
    List<AgtUser> selectList();

    /**
     * 通过管理员省份查询
     * @param province
     * @return
     */
    @Select("select au.* from agt_user au LEFT JOIN xt_user u on au.agt_province=u.user_province where u.user_province=#{province}")
    List<AgtUser> selectListByPro(String province);

    /**
     * 根据登录名删除
     */
    @Delete("delete from agt_user where login_name=#{loginName}")
    int deleteByLoginName(String loginName);

    /**
     * 此管理员有区
     * 以区和城市合并查询
     * @param loginName
     * @return
     */
    @Select("select au.* from agt_user au LEFT JOIN xt_user xu ON xu.user_city=au.agt_city and xu.user_dist =au.agt_dist where xu.login_name=#{loginName}")
    List<AgtUser> selectListHasDist(String loginName);


    /**
     * 根据身份证号查询代理商信息
     * @param cardId
     * @return
     */
    @Select("select * from agt_user where user_cardId=#{cardId}")
    AgtUser selectByCardId(String cardId);



    @Select("select au.* from xt_user xu LEFT JOIN agt_user au on xu.id=au.user_id where xu.id=#{id} and au.status=\"4\"")
    List<AgtUser> selectFailPath1(Integer id);
}