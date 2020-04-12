package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.mapper.CheckIteMapper;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceimpl implements CheckItemService{
    @Autowired
    private CheckIteMapper checkIteMapper;

    @Override
    public void add(CheckItem checkItem) {
        checkIteMapper.add(checkItem);
    }

    @Override
    public PageInfo<CheckItem> findPage(QueryPageBean queryPageBean) {
        PageHelper helper = new PageHelper();
        helper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        String queryString = queryPageBean.getQueryString();
        if (queryString != null && queryString.length()>0){
            queryString = queryString.replaceAll(" ","" );
        }
        List<CheckItem> list =  checkIteMapper.findPage(queryString);
        return new PageInfo<CheckItem>(list);
    }

    @Override
    public CheckItem findById(int id) {
        return checkIteMapper.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkIteMapper.edit(checkItem);
    }

    @Override
    public void deleteById(int id) {
        checkIteMapper.deleteById(id);
    }

//    查询中间表是否有数据
    @Override
    public void findByCheckItemId(int id) {
       long count =  checkIteMapper.findByCheckItemId(id);
       if (count>0){
           throw new RuntimeException();
       }
    }

    @Override
    public void deleteAnywhere(int id) {
//        删除中间表数据
        checkIteMapper.deleteByCheckItemId(id);
//        删除检查项数据
        checkIteMapper.deleteById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkIteMapper.findAll();
    }


}
