package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
    /*
    * 1.标注这个类为controller
    * 2.集成了ResponseBody注解的作用
    * */
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem formData){
        Result result = null;
        try {
            checkItemService.add(formData);
            result= new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result= new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return result;
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageInfo<CheckItem> pageInfo = checkItemService.findPage(queryPageBean);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @RequestMapping("/findById.do")
    public Result findById(int id){

        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }

    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    /**
     * 根据id删除检查项
     * @param id
     * @return
     */
    @RequestMapping("/deleteById.do")
    public Result deleteById( int id){
        try {
            checkItemService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }


    /**
     * 根据id查询中间表信息
     * @param id
     * @return
     */
    @RequestMapping("/findByCheckItemId.do")
    public Result findByCheckItemId( int id){
        try {
            checkItemService.findByCheckItemId(id);
            return new Result(true,"没有表数据");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"存在表数据");
        }
    }

    /**
     *删除任意位置检查项
     * @param id
     * @return
     */
    @RequestMapping("/deleteAnywhere.do")
    public Result deleteAnywhere( int id){
        try {
            checkItemService.deleteAnywhere(id);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    /**
     * 查询所有检查项
     * @return
     */
    @RequestMapping("/findAll.do")
    public Result findAll(){
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
