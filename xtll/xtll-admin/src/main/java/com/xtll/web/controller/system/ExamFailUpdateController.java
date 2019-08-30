package com.xtll.web.controller.system;


import com.xtll.common.utils.StringUtils;
import com.xtll.framework.shiro.service.SysPasswordService;
import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.BussUser;
import com.xtll.system.domain.XtUser;
import com.xtll.system.domain.dto.UpdateUser;
import com.xtll.system.service.AgtUserService;
import com.xtll.system.service.AreaService;
import com.xtll.system.service.BussUserService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExamFailUpdateController {


    private static final Log log = LogFactory.getLog(ExamFailUpdateController.class);


    @Autowired
    private BussUserService bussUserService;
    @Autowired
    private AreaService areaService;

    @Autowired
    private AgtUserService agtUserService;

    @ResponseBody
    @PostMapping("/exam1FailToUpdateAgt")
    public Map<String, Object> updateExam1ForAgt(AgtUser agtUser, @RequestParam("cardIdfile") MultipartFile crdFile, @RequestParam("certenfile") MultipartFile cerFile) {

        Map<String, Object> map = new HashMap<>();
        //用户名不重复判断
        //图片处理
        try {
            // 获取IP地址
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("你的IP地址是：" + ip);
            String path = AddController.saveFile(ip, agtUser);

            String cerFileName = "certificate";
            String newCerFileName = AddController.changeFileName(cerFile, path, cerFileName);
            String carFileName = "cardId";
            String newCardFileName = AddController.changeFileName(crdFile, path, carFileName);
            // 保存图片名到数据库
            // 代码--
            //存地址
            String newPath = "http://" + ip + ":80/profile/" + "agt/" + agtUser.getLoginName();
            agtUser.setFilePath(newPath);
            agtUser.setCertificateName(newCerFileName);
            agtUser.setCardidName(newCardFileName);
            //修改区域
            /**
             *  1.获取区域
             * 2.切割 查询cardId 添加cardId
             */
            System.out.println(agtUser.getAreasNum());
            String[] query = agtUser.getAreasNum();

            for (int i = 0; i < query.length; i++) {
                String card = areaService.checkHasAgtOrNot(query[i]);
                System.out.println(card);
                if (StringUtils.isEmpty(card) || card == agtUserService.selectByPrimaryKey(agtUser.getId()).getUserCardid()) {
                    //若果没有代理商  则插入
                    System.out.println("绑定区域");
                    System.out.println(agtUser.getUserCardid());
                    System.out.println(query[i]);
                    int num = areaService.insertCardId(agtUser.getUserCardid(), query[i]);
                } else {
                    //如果有报错
                    System.out.println("此区域已经有代理商");
                    map.put("code", "-1");
                    map.put("msg", "此区域已经有代理商，请重新选择");
                    return map;
                }
            }
            agtUser.setStatus(Short.parseShort("2"));
            agtUser.setUpdateTime(new Date(System.currentTimeMillis()));
            int num = agtUserService.updateByPrimaryKeySelective(agtUser);
            if (num > 0) {
                map.put("code", "1");
                map.put("msg", "添加成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println("报错" + e.getMessage());
            map.put("code", "-1");
            map.put("msg", "数据有误");
            return map;
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/exam1FailToUpdateBuss")
    public Map<String, Object> updateExam1ForBuss(BussUser bussUser, @RequestParam("cardIdfile") MultipartFile crdFile, @RequestParam("certenfile") MultipartFile cerFile) {
        Map<String, Object> map = new HashMap<>();
        List<BussUser> bussUserList = bussUserService.selectList();

        try {
            // 获取IP地址
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("你的IP地址是：" + ip);
            String path = AddController.saveFileBuss(ip, bussUser);

            String cerFileName = "certificate";
            String newCerFileName = AddController.changeFileName(cerFile, path, cerFileName);
            String carFileName = "cardId";
            String newCardFileName = AddController.changeFileName(crdFile, path, carFileName);
            // 保存图片名到数据库
            // 代码--
            //存地址
            String newPath = "http://" + ip + ":80/profile/" + "buss/" + bussUser.getLoginName();
            bussUser.setFilePath(newPath);
            bussUser.setCertificateName(newCerFileName);
            bussUser.setCardidName(newCardFileName);
            //添加商户关联
            String[] areaArray = bussUser.getAreasNum();
            bussUser.setAreaId(areaArray[0]);
            bussUser.setUpdateTime(new Date(System.currentTimeMillis()));
            bussUser.setStatus(Short.parseShort("2"));
            int num = bussUserService.updateByPrimaryKeySelective(bussUser);
            if (num > 0) {
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
}
