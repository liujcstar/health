package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    void add(CheckItem checkItem);

    PageInfo<CheckItem> findPage(QueryPageBean queryPageBean);

    CheckItem findById(int id);

    void edit(CheckItem checkItem);

    void deleteById(int id);

    void findByCheckItemId(int id);

    void deleteAnywhere(int id);

    List<CheckItem> findAll();
}
