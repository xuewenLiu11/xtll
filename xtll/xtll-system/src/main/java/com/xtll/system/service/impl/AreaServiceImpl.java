package com.xtll.system.service.impl;


import com.xtll.system.domain.Areas;
import com.xtll.system.domain.vo.AgtCardId;
import com.xtll.system.domain.vo.AreasPac;
import com.xtll.system.mapper.AreasMapper;
import com.xtll.system.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreasMapper areasMapper;

    @Override
    public AgtCardId selectCardIdById(String id) {
        return areasMapper.selectCardIdById(id);
    }

    @Override
    public int insertCardId(String cardId, String areaId) {
        return areasMapper.insertCardId(cardId,areaId);
    }

    @Override
    public String checkHasAgtOrNot(String areaId) {
        return areasMapper.checkHasAgtOrNot(areaId);
    }

    @Override
    public AreasPac selectAreaName(String areaId) {
        return areasMapper.selectAreaName(areaId);
    }

    @Override
    public Areas selectByPrimaryKey(String id) {
        return areasMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Areas> selectByCity(String city) {
        return areasMapper.selectByCity(city);
    }

    @Override
    public List<Areas> selectByProvince(String province) {
        return areasMapper.selectByProvince(province);
    }

    @Override
    public List<Areas> selectByCardId(String agtCardId) {
        return areasMapper.selectByCardId(agtCardId);
    }

    @Override
    public List<Areas> selectList() {

        return areasMapper.selectList();
    }


}
