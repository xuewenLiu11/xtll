package com.xtll.system.service.impl;


import com.xtll.system.domain.AllRole;
import com.xtll.system.mapper.AllRoleMapper;
import com.xtll.system.service.AllRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllRoleServiceImpl implements AllRoleService {


    @Autowired
    private AllRoleMapper allRoleMapper;


    @Override
    public List<AllRole> selectList() {
        return allRoleMapper.selectList();
    }

    @Override
    public int insert(AllRole record) {
        return allRoleMapper.insert(record);
    }

    @Override
    public int updateByPrimaryKeySelective(AllRole record) {
        return allRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return allRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public AllRole selectRoleIdByRoleName(String roleName) {
        return allRoleMapper.selectRoleIdByRoleName(roleName);
    }

    @Override
    public AllRole selectByPrimaryKey(Integer id) {
        return allRoleMapper.selectByPrimaryKey(id);
    }
}
