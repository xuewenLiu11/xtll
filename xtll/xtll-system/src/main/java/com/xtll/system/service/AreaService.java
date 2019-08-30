package com.xtll.system.service;

import com.xtll.system.domain.Areas;
import com.xtll.system.domain.vo.AgtCardId;
import com.xtll.system.domain.vo.AreasPac;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaService {




    AgtCardId selectCardIdById(String id);


    int insertCardId(String cardId,String areaId);

    String checkHasAgtOrNot(String areaId);

    AreasPac selectAreaName(String areaId);

    Areas selectByPrimaryKey(String id);

    List<Areas> selectByCity(String city);
    List<Areas> selectByProvince(String province);

    List<Areas> selectByCardId(String agtCardId);

    List<Areas> selectList();
}
