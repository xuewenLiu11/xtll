package com.xtll.system.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xtll.common.utils.commonUtils.CommonUtils;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.Areas;
import com.xtll.system.domain.GoodsAdd;
import com.xtll.system.domain.XtUser;
import com.xtll.system.domain.vo.AreasPExamGoods;
import com.xtll.system.domain.vo.AreasPac;
import com.xtll.system.mapper.AreasMapper;
import com.xtll.system.mapper.GoodsAddMapper;
import com.xtll.system.service.GoodsAddService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsAddServiceImpl implements GoodsAddService {


    @Autowired
    private GoodsAddMapper goodsAddMapper;

    @Autowired
    private AreasMapper areasMapper;

    @Override
    public PageUtils<GoodsAdd> selectPageList(Integer pageNum,Integer pageSize) {

        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<GoodsAdd> list = goodsAddMapper.selectList();
        PageUtils<GoodsAdd> result = new PageUtils<GoodsAdd>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        return result;
    }




    @Override
    public PageUtils<GoodsAdd> selectListByStatus(Integer pageNum, Integer pageSize, int[] statu,String typeFirst,String typeSecond,String typeThird) {
        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<GoodsAdd> list = goodsAddMapper.selectListByStatus(statu,typeFirst,typeSecond,typeThird);
        PageUtils<GoodsAdd> result = new PageUtils<GoodsAdd>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        return result;
    }

    @Override
    public List<AreasPExamGoods> selectListSameDist(String typeThird, String distribution) throws Exception{
        String[] string= CommonUtils.getArrayByString(distribution);
        List<AreasPExamGoods> listMap=new ArrayList<>();
        for (int i = 0; i <string.length ; i++) {
            AreasPExamGoods areasPExamGoods=new AreasPExamGoods();
            areasPExamGoods=areasMapper.selectAreaNameForExamGoods(string[i]);
            List<GoodsAdd> goodsAdds=new ArrayList<>();
            goodsAdds=goodsAddMapper.selectListSameDist(typeThird,string[i]);
            areasPExamGoods.setTotal(goodsAdds.size());
            areasPExamGoods.setRows(goodsAdds);
            listMap.add(areasPExamGoods);
        }
        return listMap;
    }

    @Override
    public GoodsAdd selectByPrimaryKey(String id) {



        return goodsAddMapper.selectByPrimaryKey(id);
    }
}
