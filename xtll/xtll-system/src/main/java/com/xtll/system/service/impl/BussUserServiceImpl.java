package com.xtll.system.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.BussUser;
import com.xtll.system.domain.XtUser;
import com.xtll.system.mapper.BussUserMapper;
import com.xtll.system.service.BussUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BussUserServiceImpl implements BussUserService {

    @Autowired
    private BussUserMapper bussUserMapper;
    @Override
    public List<BussUser> selectList() {
        return bussUserMapper.selectList();
    }

    @Override
    public List<BussUser> selectListByProv(String province) {
        return bussUserMapper.selectListByProv(province);
    }

    @Override
    public int deleteByLoginName(String loginName) {
        return bussUserMapper.deleteByLoginName(loginName);
    }

    @Override
    public int updateByPrimaryKeySelective(BussUser record) {
        return bussUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertSelective(BussUser record) {
        return bussUserMapper.insertSelective(record);
    }

    @Override
    public List<BussUser> selectListByAreas(String areaId) {
        return bussUserMapper.selectListByAreas(areaId);
    }

    @Override
    public BussUser selectByPrimaryKey(Integer id) {
        return bussUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BussUser> selectListByStatus(int[] statu) {
        return bussUserMapper.selectListByStatus(statu);
    }

    @Override
    public PageUtils<BussUser> selectListPageByStatus(int[] status, Integer pageNum, Integer pageSize) {
        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<BussUser> list = this.selectListByStatus(status);

        System.out.println(list);
        PageUtils<BussUser> result = new PageUtils<BussUser>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        return result;
    }

    @Override
    public PageUtils<BussUser> selectFailPath1(Integer id, Integer pageNum, Integer pageSize) {
        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<BussUser> list =bussUserMapper.selectFailPath1(id);
        PageUtils<BussUser> result = new PageUtils<BussUser>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        return result;
    }
}
