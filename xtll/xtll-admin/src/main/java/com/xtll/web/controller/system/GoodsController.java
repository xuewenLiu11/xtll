package com.xtll.web.controller.system;



import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonArray;
import com.xtll.common.json.JSONObject;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.Areas;
import com.xtll.system.domain.Goods;
import com.xtll.system.service.AreaService;
import com.xtll.system.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("商品功能")
@Controller
public class GoodsController {


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private AreaService areaService;

    private static final Log log = LogFactory.getLog(GoodsController.class);


    /**
     * 显示商品的列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("商品查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页数", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", required = true, paramType = "query", dataType = "String")
    })
    @ResponseBody
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    public Map<String, Object> selectList(@RequestParam(value = "pageNum", defaultValue = "1") String pageNum, @RequestParam(value = "pageSize", defaultValue = "5") String pageSize) {

        HashMap<String, Object> map = new HashMap<>();
        try {
            PageUtils<Goods> goodsPageUtils = goodsService.selectList(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
            map.put("code", 1);
            map.put("msg", "查询成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            map.put("code", -1);
            map.put("msg", "查询失败");
            return map;
        }
        return map;
    }

    /**
     * 商品的修改
     *
     * @param goods
     * @return
     */
    @ApiOperation("商品修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品id", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "商品名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "plantTime", value = "种植时间", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rate", value = "利率", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sId", value = "种植还是养殖", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "fId", value = "第二层", required = true, paramType = "query", dataType = "Integer")
    })
    @ResponseBody
    @PostMapping("/goods/update")
    public Map<String, Object> GoodsUpdate(@RequestBody Goods goods) {

        HashMap<String, Object> map = new HashMap<>();
        try {
            int num = goodsService.updateByPrimaryKeySelective(goods);
            if (num > 0) {
                map.put("code", 1);
                map.put("msg", "修改成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            map.put("code", -1);
            map.put("msg", "修改失败");
            return map;
        }
        return map;

    }

    /**
     * 商品添加
     *
     * @param goods
     * @return
     */
    @ApiOperation("商品增加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "商品名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "plantTime", value = "种植时间", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "rate", value = "利率", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sId", value = "种植还是养殖", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "fId", value = "第二层", required = true, paramType = "query", dataType = "Integer")
    })
    @ResponseBody
    @PostMapping("/goods/add")
    public Map<String, Object> GoodsAdd(@RequestBody Goods goods) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            int num = goodsService.insert(goods);
            if (num > 0) {
                map.put("code", 1);
                map.put("msg", "添加成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            map.put("code", -1);
            map.put("msg", "添加失败");
            return map;
        }
        return map;

    }

    /**
     * 商品删除
     *
     * @param id
     * @return
     */
    @ApiOperation("商品删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品id", required = true, paramType = "query", dataType = "Integer"),
    })
    @ResponseBody
    @PostMapping("/goods/del")
    public Map<String, Object> GoodsDel(@RequestBody String id) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            int num = goodsService.deleteByPrimaryKey(Integer.parseInt(id));
            if (num > 0) {
                map.put("code", 1);
                map.put("msg", "删除成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            map.put("code", -1);
            map.put("msg", "删除失败");
            return map;
        }
        return map;

    }
}