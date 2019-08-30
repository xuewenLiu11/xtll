package com.xtll.system.domain;

import com.xtll.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class Areas  {
    private String id;

    private String parentid;

    private String leveltype;

    private String name;

    private String shortname;

    private String parentpath;

    private String province;

    private String city;

    private String district;

    private String provinceshortname;

    private String cityshortname;

    private String districtshortname;

    private String provincepinyin;

    private String citypinyin;

    private String districtpinyin;

    private String citycode;

    private String zipcode;

    private String pinyin;

    private String jianpin;

    private String firstchar;

    private String lng;

    private String lat;

    private String remark1;

    private String remark2;

    private String agtCardid;


}