package com.xtll.system.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.Goods;
import com.xtll.system.domain.GoodsAdd;
import com.xtll.system.mapper.GoodsMapper;
import com.xtll.system.service.GoodsAddService;
import com.xtll.system.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {



    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 查询商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageUtils<Goods> selectList(Integer pageNum, Integer pageSize) {
        Page ps = PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = goodsMapper.selectList();
        PageUtils<Goods> result = new PageUtils<Goods>();
        result.setRows(list);
        result.setTotal(ps.getTotal());
        result.setPages(ps.getPages());
        result.setCurrentPage(pageNum);
        return result;
    }


    /**
     * 修改商品
     * @param record
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(Goods record) {
        return goodsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insert(Goods record) {
        return goodsMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }


}
