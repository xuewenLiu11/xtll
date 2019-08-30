package com.xtll.system.service.impl;


import com.xtll.system.mapper.XtMenuMapper;
import com.xtll.system.service.XtMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XtMenuServiceImpl implements XtMenuService {


    /**
     * 查询权限
     */
    @Autowired
    private XtMenuMapper xtMenuMapper;
    @Override
    public String[] selectAutho(Integer id) {
        return xtMenuMapper.selectAutho(id);
    }
}
