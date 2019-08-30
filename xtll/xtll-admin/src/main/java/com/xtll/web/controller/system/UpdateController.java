package com.xtll.web.controller.system;


import com.xtll.common.core.domain.AjaxResult;
import com.xtll.common.json.JSONObject;
import com.xtll.common.utils.StringUtils;
import com.xtll.framework.shiro.service.SysPasswordService;
import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.AllRole;
import com.xtll.system.domain.BussUser;
import com.xtll.system.domain.XtUser;
import com.xtll.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xtll.web.controller.system.AddController.getPerms;

@Api("修改功能")
@RestController
public class UpdateController {

    private static final Logger log = LoggerFactory.getLogger(AddController.class);

    @Autowired
    private AllRoleService allRoleService;
    @Autowired
    private AgtUserService agtUserService;

    @Autowired
    private BussUserService bussUserService;

    @Autowired
    private XtUserService xtUserService;

    @Autowired
    private AreaService areaService;

    /**
     * 管理员修改
     * @param xtUser
     * @return
     */

    @ApiOperation(value = "修改管理员",notes = "loginName,id,number必填字段")
    @ApiImplicitParams(
            {  @ApiImplicitParam(name="loginName",value="用户名",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name="id",value="主键",required = true,paramType = "query",dataType = "Integer"),
                    @ApiImplicitParam(name = "number", value = "编号", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name = "password", value = "密码编号1", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="telphone",value="联系方式编号2",required = true,paramType = "query",dataType = "Long"),
                    @ApiImplicitParam(name="roleName",value="角色名编号6",required = true,paramType = "query",dataType = "Integer"),
                    @ApiImplicitParam(name="power",value="权限编号5",paramType = "query",dataType = "String[]"),
                    @ApiImplicitParam(name = "name", value = "姓名编号4", paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name = "areasNum", value = "区域代号数组3", paramType = "query", dataType = "int[]")
            }
                    )
    @ResponseBody
    @PostMapping(value = "/sys/update/user")
    public Map<String, Object> updateUser(@RequestBody XtUser xtUser){

        Map<String,Object> map=new HashMap<>();

        String number=xtUser.getNumber();
        try {
            if("1".equals(number)){
                System.out.println("修改密码");
                SysPasswordService sysPasswordService=new SysPasswordService();
                System.out.println(xtUserService.selectByPrimaryKey(xtUser.getId()));
                if(xtUserService.selectByPrimaryKey(xtUser.getId())!=null){
                    String newPass=sysPasswordService.encryptPassword(xtUserService.selectByPrimaryKey(xtUser.getId()).getLoginName(),xtUser.getPassword());
                    xtUser.setPassword(newPass);

                }else{
                    map.put("code","-1");
                    map.put("msg","修改失败");
                    return map;
                }
            }else if("2".equals(number)){
                //修改telephone
            }else if("3".equals(number)){
                //修改区域
                xtUser.setUserAreas(getPerms(xtUser.getAreasNum(),map));
            }else if("6".equals(number)){
                //修改角色名
                AllRole allRole=null;
                if(!StringUtils.isEmpty(xtUser.getRoleName())){

                    allRole=allRoleService.selectRoleIdByRoleName(xtUser.getRoleName());
                    xtUser.setRoleId(allRole.getId());

                }
            }else if("5".equals(number)){
                //修改权限
                String perm=getPerms(xtUser.getPower(),map);
                if(perm.equals("")){
                    return map;
                }
                xtUser.setPerm(perm);
            }else if("4".equals(number)){
                //修改姓名
            }else{
                map.put("code","-1");
                map.put("msg","没有此修改项");

                return map;
            }

            xtUser.setUpdateTime(new Date(System.currentTimeMillis()));
            int num=xtUserService.updateByPrimaryKeySelective(xtUser);

            if(num>0){
                map.put("code","1");
                map.put("msg","修改成功");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            log.error(e.getMessage());
            map.put("code","-1");
            map.put("msg","修改失败");
            return map;
        }
        return map;
    }




   @ApiOperation(value="修改代理商",notes = "agtLoginName,id,number必填字段")
   @ApiImplicitParams(
           {  @ApiImplicitParam(name="loginName",value="用户名",required = true,paramType = "query",dataType = "String"),
                   @ApiImplicitParam(name="id",value="主键",required = true,paramType = "query",dataType = "Integer"),
                   @ApiImplicitParam(name = "number", value = "编号", required = true, paramType = "query", dataType = "String"),
                   @ApiImplicitParam(name = "password", value = "密码编号1", required = true, paramType = "query", dataType = "String"),
                   @ApiImplicitParam(name="telphone",value="联系方式编号2",required = true,paramType = "query",dataType = "telephone"),
                   @ApiImplicitParam(name = "name", value = "姓名编号4", paramType = "query", dataType = "String"),
                   @ApiImplicitParam(name = "areasNum", value = "区域代号数组3", paramType = "query", dataType = "int[]")
           }
   )
    @ResponseBody
    @PostMapping("/sys/update/agt")
    public Map<String, Object> updateAgt(@RequestBody AgtUser agtUser){
       System.out.println(agtUser);
        Map<String,Object> map=new HashMap<>();

        String number=agtUser.getNumber();
        try {
            if("1".equals(number)){
                System.out.println("修改密码");

                SysPasswordService sysPasswordService=new SysPasswordService();
                if(agtUserService.selectByPrimaryKey(agtUser.getId())!=null){
                    String newPass=sysPasswordService.encryptPassword( agtUserService.selectByPrimaryKey(agtUser.getId()).getLoginName(),agtUser.getPassword());
                    agtUser.setPassword(newPass);
                    System.out.println("新密码"+newPass);
                }else{
                    map.put("code","-1");
                    map.put("msg","修改失败");
                    return map;
                }


            }else if("2".equals(number)){
                System.out.println("修改电话号码");
                //修改telephone
            }else if("3".equals(number)){
                //修改区域
                String[] query=agtUser.getAreasNum();

                for (int i=0;i<query.length;i++) {
                    String card = areaService.checkHasAgtOrNot(query[i]);
                    System.out.println(card);
                    if (StringUtils.isEmpty(card)|| card == agtUserService.selectByPrimaryKey(agtUser.getId()).getUserCardid()) {
                        //若果没有代理商  则插入
                        int num = areaService.insertCardId(agtUser.getUserCardid(), query[i]);
                    } else {
                        //如果有报错
                        System.out.println("此区域已经有代理商");
                        map.put("code", "-1");
                        map.put("msg", "此区域已经有代理商，请重新选择");
                        return map;
                    }
                }
            }else if("4".equals(number)){
                System.out.println("修改名字");
                //修改姓名
            }else{
                map.put("code","-1");
                map.put("msg","没有此修改项");
                return map;
            }

            agtUser.setUpdateTime(new Date(System.currentTimeMillis()));
            int num=agtUserService.updateByPrimaryKeySelective(agtUser);

            if(num>0){
                map.put("code","1");
                map.put("msg","修改成功");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            map.put("code","-1");
            map.put("msg","修改失败");
            log.error(e.getMessage());
            return map;
        }
        return map;
    }

    /**
     * 修改商户
     * @param bussUser
     * @return
     */
    @ApiOperation(value="修改商户",notes = "bussLoginName,id,number必填字段")
    @ApiImplicitParams(
            {  @ApiImplicitParam(name="loginName",value="用户名",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name="id",value="主键",required = true,paramType = "query",dataType = "Integer"),
                    @ApiImplicitParam(name = "number", value = "编号", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name = "password", value = "密码编号1", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="telphone",value="联系方式编号2",required = true,paramType = "query",dataType = "telephone"),
                    @ApiImplicitParam(name = "name", value = "姓名编号4", paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name = "areasNum", value = "区域代号数组3", paramType = "query", dataType = "int[]")
            }
    )
    @ResponseBody
    @PostMapping("/sys/update/buss")
    public Map<String, Object> updateBuss(@RequestBody BussUser bussUser){
        Map<String,Object> map=new HashMap<>();

        String number=bussUser.getNumber();
        try {
            if("1".equals(number)){
                SysPasswordService sysPasswordService=new SysPasswordService();
                if(bussUserService.selectByPrimaryKey(bussUser.getId())!=null){
                    String newPass=sysPasswordService.encryptPassword( bussUserService.selectByPrimaryKey(bussUser.getId()).getLoginName(),bussUser.getPassword());
                    bussUser.setPassword(newPass);
                    System.out.println("新密码"+newPass);
                }else{
                    map.put("code","-1");
                    map.put("msg","修改失败");
                    return map;
                }
            }else if("2".equals(number)){
                //修改telephone
            }else if("3".equals(number)){
                //修改省城区
                String[] areaArray= bussUser.getAreasNum();
                bussUser.setAreaId(areaArray[0]);
            }else if("4".equals(number)){
                //修改姓名
            }else{
                map.put("code","-1");
                map.put("msg","没有此修改项");
                return map;
            }

            bussUser.setUpdateTime(new Date(System.currentTimeMillis()));
            int num=bussUserService.updateByPrimaryKeySelective(bussUser);

            if(num>0){
                map.put("code","1");
                map.put("msg","修改成功");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            log.error(e.getMessage());
            map.put("code","-1");
            map.put("msg","修改失败");
            return map;
        }
        return map;
    }




    /**
     * 修改类型
     * @param allRole
     * @return
     */
    @ApiOperation("修改类型")
    @ApiImplicitParams(
            { @ApiImplicitParam(name = "role", value = "角色名称", required = true, paramType = "query", dataType = "String"),
              @ApiImplicitParam(name="power",value="权限数组",required = true,paramType = "query",dataType = "String[]"),
              @ApiImplicitParam(name="id",value="主键",required = true,paramType = "query",dataType = "Integer")
            }
    )
    @ResponseBody
    @PostMapping("/sys/update/role")
    public Map<String,Object> updateRole(@RequestBody AllRole allRole){
        Map<String,Object> map=new HashMap<>();
                try {
                    String[] quriy=allRole.getPower();
                    String perm=getPerms(allRole.getPower(),map);
                    if(perm.equals("")){
                        return map;
                    }
                    allRole.setPerm(perm);
                    int num=allRoleService.updateByPrimaryKeySelective(allRole);
                    if(num>0){
                        map.put("code","1");
                        map.put("msg","修改成功");
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
                    System.out.println(e.getMessage());
                    map.put("code","-1");
                    map.put("msg","修改失败");
                    return map;
                }
        return map;
    }
}
