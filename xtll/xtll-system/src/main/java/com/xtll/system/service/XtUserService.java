package com.xtll.system.service;

import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.XtUser;

import java.util.List;

public interface XtUserService {



    PageUtils<XtUser> selectList(Integer pageNum, Integer pageSize);


    XtUser selectByLoginName(String loginName);

    /**
     * 查看自己
     * @param loginName
     * @return
     */
    PageUtils<XtUser> selectSelf(String loginName,Integer pageNum,Integer pageSize);


    int deleteByLoginName(String loginName);

    int insertSelective(XtUser record);

    int updateByPrimaryKeySelective(XtUser record);

    XtUser selectByPrimaryKey(Integer id);

    List<XtUser> selectList();

}
