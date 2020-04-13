package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
//        获取文件的全名
        String originalFilename = imgFile.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

//        自定义文件名
        String fileName = UUID.randomUUID().toString() + substring;

        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
