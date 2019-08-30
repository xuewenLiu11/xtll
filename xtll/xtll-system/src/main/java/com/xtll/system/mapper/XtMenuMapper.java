package com.xtll.system.mapper;

import com.xtll.system.domain.XtMenu;
import org.apache.ibatis.annotations.Select;

public interface XtMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XtMenu record);

    int insertSelective(XtMenu record);

    XtMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XtMenu record);

    int updateByPrimaryKey(XtMenu record);

    @Select("select m.menu_key from xt_menu m \n" +
            "LEFT JOIN xt_user_permiss up on m.id=up.menu_id\n" +
            "LEFT JOIN xt_user u on u.id=up.user_id where u.id=#{id}")
    String[] selectAutho(Integer id);
}