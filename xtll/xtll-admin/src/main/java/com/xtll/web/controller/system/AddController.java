package com.xtll.web.controller.system;


import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.xtll.common.core.domain.AjaxResult;
import com.xtll.common.json.JSONObject;
import com.xtll.common.utils.IpUtils;
import com.xtll.common.utils.StringUtils;
import com.xtll.framework.shiro.service.SysPasswordService;
import com.xtll.framework.shiro.session.OnlineSession;
import com.xtll.framework.util.ShiroUtils;
import com.xtll.system.domain.*;
import com.xtll.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.weaver.loadtime.Aj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

@Api(value = "增加功能")
@Controller
public class AddController {




    @Autowired
    private SysLogService sysLogService;


    @Autowired
    private BussUserService bussUserService;

    private static final Logger log = LoggerFactory.getLogger(AddController.class);
    @Autowired
    private AllRoleService allRoleService;

    @Autowired
    private XtUserService xtUserService;

    @Autowired
    private AgtUserService agtUserService;

    @Autowired
    private AreaService    areaService;


    @ApiOperation("增加类型")
    @ApiImplicitParams(
            { @ApiImplicitParam(name = "role", value = "角色名称", required = true, paramType = "query", dataType = "String"),
              @ApiImplicitParam(name="power",value="权限数组",required = true,paramType = "query",dataType = "Integer[]")
            }
             )
    @PostMapping (value = "/role/add")
    @ResponseBody
    public Map<String, Object> addRole(@RequestBody AllRole allRole,HttpServletRequest request ){

        Map<String,Object> map=new HashMap<>();
        String[] quriy=allRole.getPower();
        System.out.println(quriy);

        System.out.println("数组长度"+quriy.length);
        if(quriy.length==0){
            map.put("code","-1");
            map.put("msg","没有分配权限");
            return map;
        }else{
            String perm="";
            for (int i=0;i<quriy.length;i++){
               if(i<(quriy.length-1)){
                   perm=perm+quriy[i]+",";
                   System.out.println(perm);
               }else{
                   System.out.println("角标为"+i);
                   perm=perm+quriy[i];
               }
            }
            System.out.println(perm);
               allRole.setPerm(perm);

               try {
                   int num=allRoleService.insert(allRole);
                   if(num>0){
                       map.put("code","1");
                       map.put("msg","添加成功");
                   }
                   //日志记录

                   String ip=IpUtils.getIpAddr(request);
                   SysLog sysLog=new SysLog(UUID.randomUUID().toString(),"类型添加","admin",
                           null,"添加","/role/add",ip,new Date(System.currentTimeMillis()));
                   int num1=sysLogService.insertSelective(sysLog);
                   System.out.println(num1);
               }catch (Exception e){
                   log.error(e.getMessage());
                   map.put("code","-1");
                   map.put("msg","操作失败");
                   return map;
               }
        }

        return map;
    }







    /**
     * 增加管理员
     * 注意 ： 如果loginName重复，要返回
     *          添加密码要加密
     *          角色判断
     * @param xtUser
     * @return
     */
    @ApiOperation("增加管理员")
    @ApiImplicitParams(
            { @ApiImplicitParam(name = "name", value = "姓名", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="loginName",value="登录名",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="telephone",value="联系方式",required = true,paramType = "query",dataType = "Long"),
                    @ApiImplicitParam(name="userCardid",value="身份证",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name = "roleName", value = "角色", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="power",value="角色权限",required = true,paramType = "query",dataType = "String[]"),
                    @ApiImplicitParam(name="areasNum",value="区域代号数组",required = true,paramType = "query",dataType = "String[]"),
            }
    )

    @PostMapping ("/user/add")
    @ResponseBody
    public Map<String, Object> addUser( @RequestBody XtUser xtUser,HttpServletRequest request){
        System.out.println("进入添加管理员");
        Map<String,Object> map=new HashMap<>();

        try{
            //用户名不重复处理
            String loginName=xtUser.getLoginName();
            //查询所有用户
            List<XtUser> xtUserList=xtUserService.selectList();
            System.out.println("查询列表"+xtUserList);
            for (int i=0;i<xtUserList.size();i++){
                System.out.println("进入判断用户名是否重复");
                if(xtUserList.get(i).getLoginName().equals(loginName)){
                    System.out.println("用户名重复");
                    map.put("code","-1");
                    map.put("msg","用户名重复，请重新输入");
                    return map;
                };

                if(xtUserList.get(i).getUserCardid().equals(xtUser.getUserCardid())){
                    System.out.println("身份证号已存在");
                    map.put("code","-1");
                    map.put("msg","身份证号已存在，请您验证您的身份证号");
                    return map;
                };
            }
            //区域添加
            //判断有没有此区域的编号
            for (int i = 0; i < xtUser.getAreasNum().length; i++) {
                Areas areas=areaService.selectByPrimaryKey(xtUser.getAreasNum()[i]);
                if(areas==null){
                    map.put("code",-1);
                    map.put("msg","没有此区域");
                    return  map;
                }
            }
            xtUser.setUserAreas(getPerms(xtUser.getAreasNum(),map));
            //给密码加密
            SysPasswordService sysPasswordService=new SysPasswordService();
            String newPass=sysPasswordService.encryptPassword(xtUser.getLoginName(),xtUser.getPassword());
            xtUser.setPassword(newPass);

            //添加剩余字段的内容
            xtUser.setCreateTime( new Date(System.currentTimeMillis()));
            //根据roleName查询role表 查询roleid添加进去
            AllRole allRole=null;
           if(!StringUtils.isEmpty(xtUser.getRoleName())){
                    allRole=allRoleService.selectRoleIdByRoleName(xtUser.getRoleName());
                   xtUser.setRoleId(allRole.getId());
           }
            xtUser.setStatus("0");
           //权限分配
            String perm=this.getPerms(xtUser.getPower(),map);
            if(perm.equals("")){
                return map;
            }
                xtUser.setPerm(perm);
                int num = xtUserService.insertSelective(xtUser);
                System.out.println(num);
               if(num>0){
                   map.put("code","1");
                   map.put("msg","添加成功");
               }
               //日志记录
            String ip=IpUtils.getIpAddr(request);
            SysLog sysLog=new SysLog(UUID.randomUUID().toString(),"普通管理员添加","admin",
                    xtUser.getLoginName(),"添加","/user/add",ip,new Date(System.currentTimeMillis()));
            sysLogService.insertSelective(sysLog);
        }catch (Exception e){
            log.error(e.getMessage());
            System.out.println("报错信息是"+e.getMessage());
            map.put("code","-1");
            map.put("msg","数据有误");
            return map;
        }



        return map;
    }


    /**
     * 代理商的添加
     * 状态：1.审核通过
     *      2.一审未通过
     *      3.二审未通过
     * @param agtUser
     * @return
     */
    @ApiOperation("代理商添加")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "agtName", value = "姓名", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="agtLoginName",value="登录名",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name = "agtPassword", value = "密码", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="telphone",value="联系方式",required = true,paramType = "query",dataType = "Long"),
                    @ApiImplicitParam(name="agtCardid",value="身份证",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name = "cardIdfile", value = "身份证文件", required = true, dataType = "MultipartFile"),
                    @ApiImplicitParam(name = "certenfile", value = "营业执照文件", required = true, dataType = "MultipartFile"),
                    @ApiImplicitParam(name="areasNum",value="区域代号数组",required = true,dataType = "String[]")
            }
    )
    @PostMapping ("/agt/add")
    @ResponseBody
    public Map<String,Object> addAgt(AgtUser agtUser,@RequestParam("cardIdfile")MultipartFile crdFile,@RequestParam("certenfile")MultipartFile cerFile){
        Map<String, Object> map=new HashMap<>();
        //用户名不重复判断
        List<AgtUser> agtUserList=agtUserService.selectList();
        for (int i=0;i<agtUserList.size();i++){
            if(agtUserList.get(i).getLoginName().equals(agtUser.getLoginName())){
                map.put("code","-1");
                map.put("msg","用户名重复，请重新输入");
                return map;
            };
            if(agtUserList.get(i).getUserCardid().equals(agtUser.getUserCardid())){
                map.put("code","-1");
                map.put("msg","身份证号已存在，请您验证您的身份证号");
                return map;
            };

        }
        //图片处理
        try {
           // 获取IP地址
            String ip = InetAddress.getLocalHost().getHostAddress();
             String path= this.saveFile(ip,agtUser);
             String cerFileName="certificate";
             String newCerFileName=this.changeFileName(cerFile,path,cerFileName);
            String carFileName="cardId";
            String newCardFileName=this.changeFileName(crdFile,path,carFileName);
            // 保存图片名到数据库
            // 代码--
            //存地址
            String newPath="http://"+ip+":80/profile/"+"agt/"+agtUser.getLoginName();
            agtUser.setFilePath(newPath);
            agtUser.setCertificateName(newCerFileName);
            agtUser.setCardidName(newCardFileName);
            //获取当前登录信息
            Subject subject = SecurityUtils.getSubject();
            XtUser user = (XtUser) subject.getPrincipal();
            agtUser.setUserId(user.getId());
            agtUser.setCreateby(user.getLoginName());


            if(user.getId()==1){
                //如果当前登录的用户是超管
                //直接设置状态为二审
                agtUser.setStatus(Short.parseShort("3"));
            }else{
                //如果当前登录的用户是普通管理员
                //直接设置状态为一审。需要管理员预先审核
                agtUser.setStatus(Short.parseShort("2"));
            }
            agtUser.setCreateTime(new Date(System.currentTimeMillis()));
            //添加区域
            /**
             *  1.获取区域
             * 2.切割 查询cardId 添加cardId
             */
            System.out.println(agtUser.getAreasNum());
            String[] query=agtUser.getAreasNum();
            for (int i=0;i<query.length;i++){
                String card=areaService.checkHasAgtOrNot(query[i]);
                System.out.println(card);
                if(StringUtils.isEmpty(card)){
                    //若果没有代理商  则插入
                    System.out.println("绑定区域");
                    System.out.println(agtUser.getUserCardid());
                    System.out.println(query[i]);
                    int num=areaService.insertCardId(agtUser.getUserCardid(),query[i]);
                }else{
                    //如果有报错
                    System.out.println("此区域已经有代理商");
                    map.put("code","-1");
                    map.put("msg","此区域已经有代理商，请重新选择");
                    return map;
                }
            }

            //密码加密
            String newPass=new SysPasswordService().encryptPassword(agtUser.getLoginName(),agtUser.getPassword());
            agtUser.setPassword(newPass);
            int num=agtUserService.insertSelective(agtUser);
            System.out.println(num);
            if(num>0){
                map.put("code", "1");
                map.put("msg", "添加成功");
            }

           /* //日志记录
            SysLog sysLog=new SysLog(UUID.randomUUID().toString(),"代理商添加",user.getLoginName(),
                    agtUser.getLoginName(),"添加","/agt/add","",new Date(System.currentTimeMillis()));
            sysLogService.insertSelective(sysLog);*/
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println("报错"+e.getMessage());
            map.put("code", "-1");
            map.put("msg", "添加失败");
            return map;
        }
        return map;
    }


    /**
     * 测试上传文件方法
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/test")
    public Map<String,Object> getMui(@RequestParam("file")MultipartFile file) {

        HashMap<String,Object> map=new HashMap<>();

            System.out.println(file.getOriginalFilename());

       map.put("code",1);
        return map;
    }


    /**
     *
     * @param bussUser
     * @return
     */

    @ApiOperation("商户添加")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "bussName", value = "姓名", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="bussLoginName",value="登录名",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name = "bussPassword", value = "密码", required = true, paramType = "query", dataType = "String"),
                    @ApiImplicitParam(name="telphone",value="联系方式",required = true,paramType = "query",dataType = "Long"),
                    @ApiImplicitParam(name="bussCardid",value="身份证",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name = "cardIdfile", value = "身份证文件", required = true, dataType = "MultipartFile"),
                    @ApiImplicitParam(name = "certenfile", value = "营业执照文件", required = true, dataType = "MultipartFile"),
                    @ApiImplicitParam(name="areasNum",value="区域代号数组",required = true,dataType = "String[]")
            }
    )
    @PostMapping ("/buss/add")
    @ResponseBody
    public Map<String,Object> addBuss(BussUser bussUser,@RequestParam("cardIdfile")MultipartFile crdFile,@RequestParam("certenfile")MultipartFile cerFile){

        Map<String, Object> map=new HashMap<>();
        List<BussUser> bussUserList=bussUserService.selectList();
        for (int i=0;i<bussUserList.size();i++){
            if(bussUserList.get(i).getLoginName().equals(bussUser.getLoginName())){
                map.put("code","-1");
                map.put("msg","用户名重复，请重新输入");
                return map;
            };

            if(bussUserList.get(i).getUserCardid().equals(bussUser.getUserCardid())){
                map.put("code","-1");
                map.put("msg","此区域已经有代理商，请重新选择");
                return map;
            };

        }
        try {
            // 获取IP地址
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("你的IP地址是：" + ip);
            String path= this.saveFileBuss(ip,bussUser);

            String cerFileName="certificate";
            String newCerFileName=this.changeFileName(cerFile,path,cerFileName);
            String carFileName="cardId";
            String newCardFileName=this.changeFileName(crdFile,path,carFileName);
            // 保存图片名到数据库
            // 代码--
            //存地址
            String newPath="http://"+ip+":80/profile/"+"buss/"+bussUser.getLoginName();
            bussUser.setFilePath(newPath);
            bussUser.setCertificateName(newCerFileName);
            bussUser.setCardidName(newCardFileName);

            //关联管理员表
            Subject subject = SecurityUtils.getSubject();
            XtUser user = (XtUser) subject.getPrincipal();
            bussUser.setUserId(user.getId());
            bussUser.setCreateby(user.getLoginName());
            //代理商设置为0
            bussUser.setAgtId(0);
            if(user.getId()==1){
                //如果当前登录的用户是超管
                //直接设置状态为二审
                bussUser.setStatus(Short.parseShort("3"));
            }else{
                //如果当前登录的用户是普通管理员
                //直接设置状态为一审。需要管理员预先审核
                bussUser.setStatus(Short.parseShort("2"));
            }
            bussUser.setCreateTime(new Date(System.currentTimeMillis()));
            String newPass=new SysPasswordService().encryptPassword(bussUser.getLoginName(),bussUser.getPassword());
            bussUser.setPassword(newPass);

            //添加商户关联
           String[] areaArray= bussUser.getAreasNum();
            bussUser.setAreaId(areaArray[0]);

            int num=bussUserService.insertSelective(bussUser);
            if(num>0){
                map.put("code", "1");
                map.put("msg", "添加成功");
            }
        } catch (Exception e) {
            map.put("code", "-1");
            map.put("msg", "数据有误");
            return map;
        }
        return map;
    }
    /**
     * 将数组拼接成字符串
     * @param quriy
     * @param map
     * @return
     */

    public static String getPerms(String[] quriy,Map<String,Object> map){

        String perm = "";
        if(quriy.length==0){
            map.put("code","-1");
            map.put("msg","数据传输有误");
        }else {

            for (int i = 0; i < quriy.length; i++) {
                if (i <(quriy.length-1)) {
                    perm = perm + quriy[i] + ",";
                    System.out.println(perm);
                } else {
                    perm = perm + quriy[i];
                }
            }
            System.out.println("权限是"+perm);
        }
        return perm;
    }




    //存文件agt
    public  static String saveFile(String ip,AgtUser object){
    //进入saveFile方法
        System.out.println("你的IP地址是：" + ip);
        System.out.println(object.getLoginName());
        String path = null;

        String os=System.getProperty("os.name");
        System.out.println(os);

        if (os.toLowerCase().startsWith("win")){
            path = "D:/xtll/uploadPath/"+"agt/"+object.getLoginName();
            System.out.println("window");
        }else{
            path = "/home/xtll/uploadPath/"+"agt/"+object.getLoginName();
            System.out.println("linux");
        }

        // 判断文件夹是否存在
        File pathFile=new File(path);
        if(!pathFile.exists()) {
            //层级创建
            pathFile.mkdirs();
            System.out.println("创建文件夹");
        }
        System.out.println("存储地址"+path);
        return path;
    }

    //存文件agt
    public static String saveFileBuss(String ip,BussUser object){

        System.out.println("你的IP地址是：" + ip);
        String path = null;

        String os=System.getProperty("os.name");
        System.out.println(os);

        if (os.toLowerCase().startsWith("win")){
            path = "D:/xtll/uploadPath/"+"buss/"+object.getLoginName();
            System.out.println("window");
        }else{
            path = "/home/xtll/uploadPath/"+"buss/"+object.getLoginName();
            System.out.println("linux");
        }

        // 判断文件夹是否存在
        File pathFile=new File(path);
        if(!pathFile.exists()) {
            //层级创建
            pathFile.mkdirs();
        }
        return path;
    }



    public static String changeFileName(MultipartFile file,String path ,String newFileName) throws IOException {

        //进入重命名方法
        String fileName = file.getOriginalFilename();
        String suffix = "";
        if (fileName.indexOf(".") >= 0) {
            // 截取第一次点出现的下标
            int indexdot = fileName.indexOf(".");
            // 截取.jpg .png等图片格式
            suffix = fileName.substring(indexdot);
            //  fName = cerFileName.substring(0, cerFileName.lastIndexOf("."));
            newFileName = newFileName+"_" + System.currentTimeMillis();
            newFileName = newFileName + suffix;
            System.out.println("新的文件名fName=" + newFileName);
        }
        // 图片上传
        file.transferTo(new File(path, newFileName));
        System.out.println("存储成功");
        return newFileName;
    }
}
