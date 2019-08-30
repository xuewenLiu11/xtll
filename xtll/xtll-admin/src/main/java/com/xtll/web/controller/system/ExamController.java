package com.xtll.web.controller.system;


import com.xtll.common.utils.pageUtils.PageUtils;
import com.xtll.framework.util.ShiroUtils;
import com.xtll.system.domain.AgtUser;
import com.xtll.system.domain.BussUser;
import com.xtll.system.domain.vo.ExamVO;
import com.xtll.system.service.AgtUserService;
import com.xtll.system.service.BussUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(value = "审核api")
@Controller
public class ExamController {


    private static final Logger log = LoggerFactory.getLogger(AddController.class);

    @Autowired
    private AgtUserService agtUserService;


    @Autowired
    private BussUserService bussUserService;

    /**
     * 如果审核通过传我""
     * @param exam
     * @return
     */
    @ApiOperation(value = "审核")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name="type",value="审核类型",required = true,paramType = "query",dataType = "String"),
                    @ApiImplicitParam(name="id",value="唯一值",required = true,paramType = "query",dataType = "Integer"),
                    @ApiImplicitParam(name="status",value = "状态",required = true,paramType = "query",dataType = "String"),
                   @ApiImplicitParam(name="reason",value = "理由",required = true,paramType = "query",dataType = "String")

            }
    )
    @ResponseBody
    @PostMapping(value = "/exam")
    public Map<String,Object> exam(@RequestBody ExamVO exam){
        Map<String,Object> map=new HashMap<>();

        try {
            AgtUser agtUser = new AgtUser();
            BussUser bussUser = new BussUser();
            if(exam.getReason().equals("")) {
                //表示审核通过
                if ("2".equals(exam.getType())) {
                    //代理商的审核

                    String status="2".equals(exam.getStatus())?"3":"1";
                    agtUser.setStatus(Short.parseShort(status));
                    agtUser.setId(exam.getId());
                    agtUserService.updateByPrimaryKeySelective(agtUser);

                } else if ("3".equals(exam.getType())) {
                    //商户审核
                    String status="2".equals(exam.getStatus())?"3":"1";
                    bussUser.setStatus(Short.parseShort(status));
                    bussUser.setId(exam.getId());
                    bussUserService.updateByPrimaryKeySelective(bussUser);

                }
            }else{
                //表示审核失败
                if ("2".equals(exam.getType())){
                    //代理商审核失败
                    //状态为2表示一审失败
                   String status="2".equals(exam.getStatus())?"4":"5";
                   agtUser.setStatus(Short.parseShort(status));
                    agtUser.setId(exam.getId());
                    agtUser.setRemark(exam.getReason());
                    agtUserService.updateByPrimaryKeySelective(agtUser);

                }else{
                    String status="2".equals(exam.getStatus())?"4":"5";
                    bussUser.setStatus(Short.parseShort(status));
                    bussUser.setId(exam.getId());
                    bussUser.setRemark(exam.getReason());
                    int num=bussUserService.updateByPrimaryKeySelective(bussUser);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            map.put("code",-1);
            map.put("msg","审核失败");
            return map;
        }
        map.put("code",1);
        map.put("msg","审核成功");
        return map;
    }

    /**
     * 查询当前登录用户一审未通过的代理商和商户
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    @RequestMapping(value = "/fail/exam1",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> exam1Fail(@RequestParam("pageNum")String pageNum,@RequestParam("pageSize")String pageSize,@RequestParam("type")String type){

        Map<String,Object> map=new HashMap<>();
        try {
            Integer id=ShiroUtils.getXtUser().getId();

            if("2".equals(type)){
                PageUtils<AgtUser> pageUtils= agtUserService.selectFailPath1(id,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
                map.put("code",1);
                map.put("msg","查询成功");
                map.put("list",pageUtils);
            }else{
                PageUtils<BussUser> pageUtils= bussUserService.selectFailPath1(id,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
                map.put("code",1);
                map.put("msg","查询成功");
                map.put("list",pageUtils);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            map.put("code",-1);
            map.put("msg","查询失败");
            return map;
        }

        return map;
    }
}
