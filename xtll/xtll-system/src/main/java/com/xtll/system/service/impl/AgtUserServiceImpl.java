package com.xtll.system.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.XtUser;
import com.xtll.system.mapper.AgtUserMapper;
import com.xtll.system.service.AgtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgtUserServiceImpl implements AgtUserService {


    @Autowired
    private AgtUserMapper agtUserMapper;

    @Override
    public List<AgtUser> selectList() {
        return agtUserMapper.selectList();
    }

    @Override
    public List<AgtUser> selectListByPro(String province) {
        return agtUserMapper.selectListByPro(province);
    }

    @Override
    public int deleteByLoginName(String loginName) {
        return agtUserMapper.deleteByLoginName(loginName);
    }


    @Override
    public int updateByPrimaryKeySelective(AgtUser record) {
        return agtUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertSelective(AgtUser record) {
        return agtUserMapper.insertSelective(record);
    }

    @Override
    public AgtUser selectByPrimaryKey(Integer id) {
        return agtUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public AgtUser selectByCardId(String cardId) {
        return agtUserMapper.selectByCardId(cardId);
    }

    @Override
    public List<AgtUser> selectListByStatus(int[] statu) {
        return agtUserMapper.selectListByStatus(statu);
    }

    @Override
    public PageUtils<AgtUser> selectListPageByStatus(int[] statu, Integer pageNum, Integer pageSize) {
        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<AgtUser> list = this.selectListByStatus(statu);
        System.out.println(list);
        PageUtils<AgtUser> result = new PageUtils<AgtUser>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        return result;

    }

    @Override
    public PageUtils<AgtUser> selectFailPath1(Integer id, Integer pageNum, Integer pageSize) {

        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<AgtUser> list = agtUserMapper.selectFailPath1(id);
        System.out.println(list);
        PageUtils<AgtUser> result = new PageUtils<AgtUser>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        return result;
    }
}
