package com.xtll.system.mapper;

import com.xtll.system.domain.AllRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AllRoleMapper {
    /**
     * 插入
     * @param record
     * @return
     */
    int insert(AllRole record);

    int insertSelective(AllRole record);

    /**
     * 根据id查询name
     * @param id
     * @return
     */
    AllRole selectByPrimaryKey(Integer id);

    @Select("select * from all_role")
    List<AllRole> selectList();

    /**
     * 修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(AllRole record);


    int deleteByPrimaryKey(Integer id);


    /**
     * 根据roleName 查询roleId
     * 关联到用户表
     */
    @Select("select * from all_role where role=#{roleName}")
    AllRole selectRoleIdByRoleName(String roleName);

}