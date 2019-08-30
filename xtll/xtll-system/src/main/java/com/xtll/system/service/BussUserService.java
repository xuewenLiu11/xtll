package com.xtll.system.service;

import com.github.pagehelper.Page;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.BussUser;

import java.util.List;

public interface BussUserService {

    List<BussUser> selectList();

    /**
     * 根据管理员省份查询对应的商户
     * @param province
     * @return
     */
    List<BussUser> selectListByProv(String province);

    int deleteByLoginName(String loginName);

    int updateByPrimaryKeySelective(BussUser record);

    int insertSelective(BussUser record);

    List<BussUser> selectListByAreas(String areaId);

    BussUser selectByPrimaryKey(Integer id);

    List<BussUser> selectListByStatus(int[] statu);

    PageUtils<BussUser> selectListPageByStatus(int[] status, Integer pageNum, Integer pageSize);

    PageUtils<BussUser> selectFailPath1(Integer id,Integer pageNum,Integer pageSize);

}
