package com.xtll.web.controller.system;


import com.xtll.common.utils.StringUtils;
import com.xtll.common.utils.commonUtils.CommonUtils;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.system.domain.Areas;
import com.xtll.system.domain.Goods;
import com.xtll.system.domain.GoodsAdd;
import com.xtll.system.domain.vo.AreasPExamGoods;
import com.xtll.system.domain.vo.AreasPac;
import com.xtll.system.service.AreaService;
import com.xtll.system.service.GoodsAddService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("审核列表")
@Controller
public class GoodsExamController {


    private final static Log log= LogFactory.getLog(GoodsExamController.class);
    @Autowired
    private GoodsAddService goodsAddService;


    @Autowired
    private AreaService   areaService;


        /**
         * 显示商品审核列表
         * 待审核  已审核
         * 传参  ：
         *      status 1--已审核
         *              2--待审核
         *              3--未通过
         *      goodsType 字符串
         *      如果是0  代表没有类型的选择
         * @param pageNum
         * @param pageSize
         * @param status
         * @return
         */
      @ApiOperation("商品审核列表")
      @ApiImplicitParams(
              {
                      @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, paramType = "query", dataType = "String"),
                      @ApiImplicitParam(name="pageSize",value="页大小",required = true,paramType = "query",dataType = "String"),
                      @ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "query", dataType = "String"),
                      @ApiImplicitParam(name="goodsType",value="类别",required = true,paramType = "query",dataType = "Long"),
              }
      )
     @RequestMapping(value = "/goodsExam",method = RequestMethod.GET)
     @ResponseBody
     public Map<String,Object> selectList(@RequestParam("pageNum")String pageNum,@RequestParam("pageSize")String pageSize,
                                             @RequestParam("status")String status,@RequestParam("goodsType")String goodsType){
         Map<String,Object> map=new HashMap<>();
         if(StringUtils.isEmpty(status)){
             map.put("code",-1);
             map.put("msg","请传入审核标识");
            return map;
         }
         int[] statu=CommonUtils.getIntByString(status);
         String[] goodT=CommonUtils.getArrayByString(goodsType);
         PageUtils<GoodsAdd> goodsAdds=new PageUtils<>();
        try {
            if(goodT.length>1){
                 goodsAdds=goodsAddService.selectListByStatus(Integer.parseInt(pageNum),Integer.parseInt(pageSize),statu,goodT[0],goodT[1],goodT[2]);

            }else{
                 goodsAdds=goodsAddService.selectListByStatus(Integer.parseInt(pageNum),Integer.parseInt(pageSize),statu,null,null,null);
            }
            List<GoodsAdd> list=goodsAdds.getRows();
            for (GoodsAdd goodsAdd:list) {
                List<String> areaN=new ArrayList<>();
                List<AreasPac> areasPacList=new ArrayList<>();
                String[] goodDis=CommonUtils.getArrayByString(goodsAdd.getDistribution());
                for (int i = 0; i <goodDis.length ; i++) {
                    Areas areas=areaService.selectByPrimaryKey(goodDis[i]);
                    //封装areaNums开始
                    String areaP=areas.getParentpath();
                    areaN.add(areaP);
                    //封装areaNums结束
                    AreasPac areasPac=new AreasPac();
                    areasPac.setProvince(areas.getProvince());areasPac.setCity(areas.getCity());areasPac.setDistrict(areas.getDistrict());
                    areasPacList.add(areasPac);
                }
            goodsAdd.setAreaNums(areaN);
            goodsAdd.setAreas(areasPacList);
            }
            System.out.println(goodsAdds);
            map.put("code",1);
            map.put("msg","查询成功");
            map.put("list",goodsAdds);
        }catch (Exception e){
            map.put("code",-1);
            map.put("msg","查询失败");
            return map;
        }
        return map;
    }


    /**
     * 商品审核页面
     * 点击区域的查看 进行比较
     * 查看当前商品的当前区域的商户数量
     *  1.要审核通过的
     *  2.要同商品的
     *  3.要同区域的
     *
     * @param id
     * @return
     */
    @ApiOperation("商品审核关于区域的详情")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "数据的唯一标识", required = true, paramType = "query", dataType = "String"),
            }
    )
    @ResponseBody
    @RequestMapping(value = "/goodsExam/view",method = RequestMethod.GET)
    public HashMap<String,Object> view(@RequestParam("id")String id){
        HashMap<String,Object> map=new HashMap<>();
        List<AreasPExamGoods> mapList=new ArrayList<>();
      try {
            GoodsAdd goodsAdd=goodsAddService.selectByPrimaryKey(id);
           if(goodsAdd!=null){
               mapList=goodsAddService.selectListSameDist(goodsAdd.getTypeThird(),goodsAdd.getDistribution());
               map.put("code","查询成功");
               map.put("map",mapList);
           }else{
              map.put("code",-1);
              map.put("msg","没有此条记录");
              return map;
          }
      }catch (Exception e){
          log.error(e.getMessage());
          System.out.println(e.getMessage());
          map.put("code",-1);
          map.put("msg","查看失败");
         return map;

      }

        return  map;


    }

}
