package com.xtll.system.service.impl;


import com.xtll.system.domain.SysLog;
import com.xtll.system.mapper.SysLogMapper;
import com.xtll.system.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl implements SysLogService {


    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public int insertSelective(SysLog record) {
        return sysLogMapper.insertSelective(record);
    }
}
