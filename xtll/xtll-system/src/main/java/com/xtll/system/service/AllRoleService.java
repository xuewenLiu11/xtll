package com.xtll.system.service;


import com.xtll.system.domain.AllRole;

import java.util.List;

public interface AllRoleService {

    List<AllRole> selectList();

    int insert(AllRole record);

    int updateByPrimaryKeySelective(AllRole record);

    int deleteByPrimaryKey(Integer id);

    AllRole selectRoleIdByRoleName(String roleName);
    AllRole selectByPrimaryKey(Integer id);
}
