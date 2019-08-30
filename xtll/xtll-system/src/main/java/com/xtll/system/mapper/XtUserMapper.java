package com.xtll.system.mapper;

import com.xtll.system.domain.XtUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface XtUserMapper {
        int deleteByPrimaryKey(Integer id);

        int insert(XtUser record);

        /**
         * 添加
         * @param record
         * @return
         */
        int insertSelective(XtUser record);


        /**
         *
         * @param id
         * @return
         */
        XtUser selectByPrimaryKey(Integer id);


        /**
         * 修改用户
         * @param record
         * @return
         */
        int updateByPrimaryKeySelective(XtUser record);

        int updateByPrimaryKey(XtUser record);


        /**
         * 通过用户名查询用户
         * @param loginName
         * @return
         */
        @Select("select * from xt_user where login_name=#{loginName}")
        XtUser selectByLoginName(String loginName);


        /**
         * 查询列表
         */
        @Select("select * from xt_user")
        List<XtUser> selectList();

        /**
         * 查询自己
         */
        @Select("select * from xt_user where login_name=#{loginName}")
        List<XtUser> selectSelf(String loginName);


        /**
         * 根据loginName删除对象
         */
        @Delete("delete from xt_user where login_name=#{loginName}")
        int deleteByLoginName(String loginName);




}