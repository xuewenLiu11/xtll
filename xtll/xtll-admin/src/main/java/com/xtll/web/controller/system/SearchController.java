package com.xtll.web.controller.system;


import ch.qos.logback.core.encoder.EchoEncoder;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysql.cj.xdevapi.JsonArray;
import com.xtll.common.core.domain.AjaxResult;
import com.xtll.common.core.page.TableDataInfo;

import com.xtll.common.json.JSONObject;
import com.xtll.common.utils.StringUtils;
import com.xtll.common.utils.commonUtils.CommonUtils;
import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.framework.util.ShiroUtils;
import com.xtll.system.domain.*;
import com.xtll.system.domain.vo.AgtCardId;
import com.xtll.system.domain.vo.AreasIdAndName;
import com.xtll.system.domain.vo.AreasPac;
import com.xtll.system.domain.vo.AreasPacForAgtAndBuss;
import com.xtll.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.spring.web.json.Json;

import java.util.*;

import static com.github.pagehelper.page.PageMethod.startPage;

@Api(value = "查询列表")
@Controller
public class SearchController {


    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private XtUserService xtUserService;

    @Autowired
    private BussUserService bussUserService;

    @Autowired
    private AgtUserService agtUserService;

    @Autowired
    private AllRoleService allRoleService;

    @Autowired
    private AreaService areaService;

    /**
     * @param
     * @return
     */
    @ApiOperation("查询管理员")
    @RequestMapping(value = "/user/selectList", method = RequestMethod.GET)
    @ResponseBody
    public Map selectListUser(@RequestParam(value = "pageNum", defaultValue = "1") String pageNum, @RequestParam(value = "pageSize", defaultValue = "5") String pageSize) {

        //XtUser xtUser = ShiroUtils.getXtUser();
        Map<String, Object> map = new HashMap<>();
        PageUtils<XtUser> dataList = new PageUtils();
        try {
            if (/*xtUser.isAdmin()*/1==1) {
                System.out.println("是超管");
                dataList = xtUserService.selectList(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
                List<XtUser> list =dataList.getRows();
                for (XtUser user : list) {
                    List<String> areaN=new ArrayList<>();
                    List<AreasPac> areasPacList = new ArrayList<>();
                    String[] areasArray =CommonUtils.getArrayByString(user.getUserAreas());
                    for (int i = 0; i < areasArray.length; i++) {
                        AreasPac areasPac = new AreasPac();
                           Areas areas=areaService.selectByPrimaryKey(areasArray[i]);
                           String parentPath=areas.getParentpath();
                            areasPac.setProvince(areas.getProvince());
                            areasPac.setCity(areas.getCity());
                            areasPac.setDistrict(areas.getDistrict());
                             areaN.add(parentPath);
                            areasPacList.add(areasPac);
                    }
                    user.setAreaNums(areaN);
                    user.setAreas(areasPacList);
                }
            } else {
                System.out.println("不是超管");
                dataList = xtUserService.selectSelf(/*xtUser.getLoginName()*/"", Integer.parseInt(pageNum), Integer.parseInt(pageSize));
                List<XtUser> list = dataList.getRows();
                for (XtUser userBy:list) {
                    List<String> areaN=new ArrayList<>();
                    String[] areaArray=CommonUtils.getArrayByString(userBy.getUserAreas());
                    for (int i = 0; i <areaArray.length ; i++) {
                        if(!StringUtils.isEmpty(areaService.selectByPrimaryKey(areaArray[i]).getParentpath())){
                            areaN.add(areaService.selectByPrimaryKey(areaArray[i]).getParentpath());
                        };
                    }
                 userBy.setAreaNums(areaN);
                }
                String[] areasArray =CommonUtils.getArrayByString(list.get(0).getUserAreas());
                AreasPac areasPac = new AreasPac();
                List<AreasPac> areasPacList1 = new ArrayList<>();
                for (int i = 0; i <areasArray.length ; i++) {
                    areasPac = areaService.selectAreaName(areasArray[i]);
                    areasPacList1.add(areasPac);
                    list.get(0).setAreas(areasPacList1);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            map.put("code", "-1");
            map.put("msg", "查询失败");
            return map;
        }
        map.put("dataList", dataList);
        map.put("code", "1");
        map.put("msg", "查询成功");
        return map;
    }

    /**
     * 如果不是超管
     *
     * @return
     */

    @ApiOperation("查询代理商")
    @RequestMapping(value = "/agt/selectList", method = RequestMethod.GET)
    @ResponseBody
    public Map selectListAgt(@RequestParam(value = "sort") String sort,@RequestParam(value = "pageNum", defaultValue = "1") String pageNum,@RequestParam(value = "pageSize", defaultValue = "5")String pageSize) {
        int[] statu =CommonUtils.getIntByString(sort);
        XtUser xtUser = ShiroUtils.getXtUser();
        Map<String, Object> map = new HashMap<>();
        List<AgtUser> agtList=new ArrayList<>();
        PageUtils dataList =new PageUtils();
        try {
            if (xtUser.isAdmin()) {
                System.out.println("是超管");
                dataList = agtUserService.selectListPageByStatus(statu,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
                agtList= dataList.getRows();
                agtList=this.pacAreas(agtList);
            } else {
                //不是管理员
                String areas = xtUser.getUserAreas();
                //String areas="630000,630100,630102,120000";
                String[] areaArray = areas.split(",");
                for (int i = 0; i < areaArray.length; i++) {
                    //根据代号查询区域
                    Areas areas1 = areaService.selectByPrimaryKey(areaArray[i]);
                    //看此区域是否有区，若是有区直接查询其代理商
                    if (!"".equals(areas1.getDistrict())) {
                        if (!StringUtils.isEmpty(areas1.getAgtCardid())) {
                            AgtUser agtUser = agtUserService.selectByCardId(areas1.getAgtCardid());
                            agtList.addAll(this.getAgtList(statu, agtUser));
                        }
                    }else {
                            //如果市不为null
                            if (!"".equals(areas1.getCity())) {
                                List<Areas> listByCity = areaService.selectByCity(areas1.getCity());
                                for (Areas areas2 : listByCity) {
                                    if (!StringUtils.isEmpty(areas2.getAgtCardid())) {
                                        AgtUser agtUser = agtUserService.selectByCardId(areas2.getAgtCardid());
                                        agtList.addAll(this.getAgtList(statu, agtUser));
                                    }
                                }
                            } else {
                                List<Areas> listByProvince = areaService.selectByProvince(areas1.getProvince());
                                for (Areas areas3 : listByProvince) {
                                    if (!StringUtils.isEmpty(areas3.getAgtCardid())) {
                                        AgtUser agtUser = agtUserService.selectByCardId(areas3.getAgtCardid());
                                        agtList.addAll(this.getAgtList(statu, agtUser));
                                    }
                                }
                            }
                        }
                }
                agtList=this.pacAreas(agtList);
                agtList=this.removeDuplicate(agtList);
                Page ps = PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
                List<AgtUser> list =agtList;
                dataList.setRows(list);
                if(list.size()!=0){
                    dataList.setTotal(Long.parseLong(list.size()+""));
                }else{
                    dataList.setTotal(0L);
                }
                dataList.setPages(ps.getPages());
                dataList.setCurrentPage(Integer.parseInt(pageNum));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            map.put("code", "-1");
            map.put("msg", "查询失败");
            System.out.println(e.getMessage());
            return map;
        }
        map.put("dataList", dataList);
        map.put("code", "1");
        map.put("msg", "查询成功");
        return map;

    }

    /**
     * 查询商户
     * 1.查询当前用户的区域
     * <p>
     * 3.获取到每个区域，
     * 4.通过区域id查询商户信息 返回list集合
     * list结合吞并list
     * 5.添加到集合里面
     *
     * @return
     */
    @ApiOperation("查询商户")
    @RequestMapping(value = "/buss/selectList", method = RequestMethod.GET)
    @ResponseBody
    public Map selectListBuss(@RequestParam(value = "sort") String sort,@RequestParam(value = "pageNum", defaultValue = "1") String pageNum,@RequestParam(value = "pageSize", defaultValue = "5")String pageSize) {
        XtUser xtUser = ShiroUtils.getXtUser();
        int[] statu =CommonUtils.getIntByString(sort);
        Map<String, Object> map = new HashMap<>();
        List<BussUser> bussList = new ArrayList<>();
        PageUtils<BussUser> buList=new PageUtils<>();
        try {
            if (xtUser.isAdmin()) {
                System.out.println("是超管");
                buList = bussUserService.selectListPageByStatus(statu,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
                List<BussUser> lists=buList.getRows();
                lists= this.pacAreaForBuss(lists);

            } else {
                //如果不是超管
                String areas = xtUser.getUserAreas();
               // String areas="630102,120000";
                String[] areaArray = areas.split(",");

                for (int i = 0; i < areaArray.length; i++) {
                    System.out.println("当前管理员管理的区域是" + areaArray[i]);

                    Areas areasBuss = areaService.selectByPrimaryKey(areaArray[i]);
                    if(!StringUtils.isEmpty(areasBuss.getDistrict())){
                        //有区
                        List<BussUser> buListOneArea = bussUserService.selectListByAreas(areaArray[i]);
                        for (BussUser bussUser:buListOneArea) {
                            if(!StringUtils.isEmpty(bussUser.getAreaId())){
                                bussList.addAll(this.getBussList(statu,bussUser));
                            }
                        }

                    }else{
                        //没有区
                        if(!StringUtils.isEmpty(areasBuss.getCity())){
                            System.out.println("城市代理");
                            List<Areas> listByCity = areaService.selectByCity(areasBuss.getCity());
                            this.getBuss(listByCity,statu,bussList);

                        }else{
                            System.out.println("省级代理");
                            List<Areas> listByProvince = areaService.selectByProvince(areasBuss.getProvince());
                            this.getBuss(listByProvince,statu,bussList);
                        }
                    }
                }

                bussList= this.pacAreaForBuss(bussList);
                Page ps = PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
                List<BussUser> list =bussList;
                System.out.println(list);
                buList.setRows(list);
                if(list.size()!=0){
                    buList.setTotal(Long.parseLong(list.size()+""));
                }else{
                    buList.setTotal(0L);
                }
                buList.setPages(ps.getPages());
                buList.setCurrentPage(Integer.parseInt(pageNum));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            map.put("code", "-1");
            map.put("msg", "查询失败");
        }
        map.put("dataList", buList);
        map.put("code", "1");
        map.put("msg", "查询成功");
        return map;

    }


    @ApiOperation("角色列表显示，不需要传参")
    @RequestMapping(value = "/role/selectList", method = RequestMethod.GET)
    @ResponseBody
    public Map selectRole() {

        Map<String, Object> map = new HashMap<>();
        List<AllRole> list = null;
        try {
            list = allRoleService.selectList();

        } catch (Exception e) {
            map.put("code", "-1");
            map.put("msg", "操作异常");
            log.error(e.getMessage());
        }
        map.put("data", list);
        map.put("msg", "操作成功");
        map.put("code", "1");
        return map;

    }


    public List<AgtUser> getAgtList(int[] statu, AgtUser agtUser) {
        List<AgtUser> agList=new ArrayList<>();
        if (statu.length == 3) {
            if (agtUser.getStatus() == statu[0] || agtUser.getStatus() == statu[1]||agtUser.getStatus()==statu[2]) {
                agList.add(agtUser);
            }
        } else if (statu.length == 2) {
            if (agtUser.getStatus() == statu[0] || agtUser.getStatus() == statu[1]) {
                agList.add(agtUser);
            }
        }else {
                if (agtUser.getStatus() == statu[0]) {
                    agList.add(agtUser);
                }
            }
        return agList;
    }

    public List<BussUser> getBussList(int[] statu, BussUser bussUser) {
        List<BussUser> buList=new ArrayList<>();
        if (statu.length == 3) {
            if (bussUser.getStatus() == statu[0] || bussUser.getStatus() == statu[1]||bussUser.getStatus()==statu[2]) {
                buList.add(bussUser);
            }
        } else if (statu.length == 2) {
            if (bussUser.getStatus() == statu[0] || bussUser.getStatus() == statu[1]) {
                buList.add(bussUser);
            }
        }else {
            if (bussUser.getStatus() == statu[0]) {
                buList.add(bussUser);
            }
        }
        return buList;
    }

    public void getBuss(List<Areas> list,int[] statu,List<BussUser> bussList){
        for (Areas areaCity:list) {
            List<BussUser> buListOneArea = bussUserService.selectListByAreas(areaCity.getId());
            for (BussUser bussUser:buListOneArea) {
                if(!StringUtils.isEmpty(bussUser.getAreaId())){
                    bussList.addAll(this.getBussList(statu,bussUser));
                }
            }
        }
    }

    /**
     * list集合去重
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }


    public  List<AgtUser> pacAreas(List<AgtUser> agtList){
        for (AgtUser agtUser:agtList) {
            List<AreasPac> areasPacList=new ArrayList<>();
            List<String> stringList=new ArrayList<>();
            List<Areas> areasList=areaService.selectByCardId(agtUser.getUserCardid());
            for (Areas areas:areasList){
                AreasPac areasPac=new AreasPac();
                String parentPath=areas.getParentpath();
                areasPac.setProvince(areas.getProvince());areasPac.setCity(areas.getCity());areasPac.setDistrict(areas.getDistrict());
                stringList.add(parentPath);
                areasPacList.add(areasPac);
            }
            agtUser.setAreaNums(stringList);
            agtUser.setAreas(areasPacList);
        }
        return agtList;
    }

    public List<BussUser> pacAreaForBuss(List<BussUser> lists){
        for (BussUser bussUser:lists) {
            List<String> stringList=new ArrayList<>();
            List<AreasPac> areasPacList=new ArrayList<>();
            Areas areas=areaService.selectByPrimaryKey(bussUser.getAreaId());
            String parentPath=areas.getParentpath();
            stringList.add(parentPath);
            bussUser.setAreaNums(stringList);

            AreasPac areasPac=new AreasPac();
            areasPac.setProvince(areas.getProvince());areasPac.setCity(areas.getCity());areasPac.setDistrict(areas.getDistrict());
            areasPacList.add(areasPac);
            bussUser.setAreas(areasPacList);

        }
        return lists;
    }

}
