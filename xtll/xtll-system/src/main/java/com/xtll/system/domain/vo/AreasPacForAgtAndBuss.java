package com.xtll.system.domain.vo;


import lombok.Data;

import java.util.Map;

@Data
public class AreasPacForAgtAndBuss {

    private String id;

    private AreasIdAndName province;

    private AreasIdAndName city;

    private AreasIdAndName distinct;


}
