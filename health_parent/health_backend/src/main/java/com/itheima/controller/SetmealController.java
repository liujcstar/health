package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.DelSetmealPojo;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private JedisPool jedisPool;

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

            //上传成功将图片存进redis
            addRedis(fileName);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.findPage(queryPageBean);
    }


    @RequestMapping("/deleteById")
    public Result deleteById(@RequestBody DelSetmealPojo delSetmealPojo) {
        try {
            setmealService.deleteById(delSetmealPojo);
            return new Result(true, MessageConstant.DELETE_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MEMBER_FAIL);
        }
    }

    @RequestMapping("/findCheckgroupIdBySetmealId")
    public Result findCheckgroupIdBySetmealId(Integer setmealId) {
        try {
            List<Integer> checkgroupIds =  setmealService.findCheckgroupIdBySetmealId(setmealId);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer setmealId) {
        try {
            Setmeal setmeal = setmealService.findById(setmealId);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        try {
             setmealService.edit(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/delUpload")
    public void delUpload(String oldImg) {
        addRedis(oldImg);
    }

    public void addRedis(String imgName){
        Jedis resource = jedisPool.getResource();
        resource.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
        resource.close();//释放连接

    }
}
