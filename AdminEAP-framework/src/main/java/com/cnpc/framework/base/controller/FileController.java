package com.cnpc.framework.base.controller;

import com.cnpc.framework.base.entity.SysFile;
import com.cnpc.framework.base.service.SysFileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by billJiang on 2017/3/5.
 * e-mail:475572229@qq.com  qq:475572229
 * 文件上传下载控制器,部分业务方法卸载UploaderController中
 */
@Controller
@RequestMapping(value="/file")
public class FileController {


    @Resource
    private SysFileService sysFileService;

    @RequestMapping(value="/findByFormId",method = RequestMethod.POST)
    @ResponseBody
    public SysFile findByFormId (String formId){
        return sysFileService.findByFormId(formId);
    }
}
