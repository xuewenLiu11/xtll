package com.xtll.system.mapper;

import com.xtll.system.domain.SysLog;

public interface SysLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysLog record);


    /**
     * 添加日志
     * @param record
     * @return
     */
    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);
}