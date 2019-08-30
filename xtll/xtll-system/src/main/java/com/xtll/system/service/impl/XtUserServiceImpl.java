package com.xtll.system.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.XtUser;
import com.xtll.system.mapper.XtUserMapper;
import com.xtll.system.service.XtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class XtUserServiceImpl implements XtUserService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private XtUserMapper xtUserMapper;


    @Override
    public PageUtils<XtUser> selectList(Integer pageNum,Integer pageSize) {

        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<XtUser> list = xtUserMapper.selectList();

        System.out.println(list);
        PageUtils<XtUser> result = new PageUtils<XtUser>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        return result;
    }

    @Override
    public XtUser selectByLoginName(String loginName) {
        return xtUserMapper.selectByLoginName(loginName);
    }

    @Override
    public PageUtils<XtUser> selectSelf(String loginName,Integer pageNum,Integer pageSize) {
        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<XtUser> list = xtUserMapper.selectSelf(loginName);

        System.out.println(list);
        PageUtils<XtUser> result = new PageUtils<XtUser>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        System.out.println("总条数   "+ps.getTotal());
        System.out.println("总页数   "+ps.getPages());
        return result;
    }

    @Override
    public int deleteByLoginName(String loginName) {
        return xtUserMapper.deleteByLoginName(loginName);
    }

    @Override
    public int insertSelective(XtUser record) {
        return xtUserMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(XtUser record) {
        return xtUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public XtUser selectByPrimaryKey(Integer id) {
        return xtUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<XtUser> selectList() {
        return xtUserMapper.selectList();
    }


}
