package com.xtll.system.service;

import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.XtUser;
import com.xtll.system.domain.vo.AgtCardId;

import java.util.List;

public interface AgtUserService {

    List<AgtUser> selectList();

    List<AgtUser> selectListByPro(String province);

    int deleteByLoginName(String loginName);


    int updateByPrimaryKeySelective(AgtUser record);

    int insertSelective(AgtUser record);

    AgtUser selectByPrimaryKey(Integer id);

    AgtUser selectByCardId(String cardId);

    List<AgtUser> selectListByStatus(int[] statu);

    PageUtils<AgtUser> selectListPageByStatus(int[] statu, Integer pageNum, Integer pageSize);


    PageUtils<AgtUser> selectFailPath1(Integer id,Integer pageNum,Integer pageSize);

}
