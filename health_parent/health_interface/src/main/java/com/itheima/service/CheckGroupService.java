package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    PageResult findPage(QueryPageBean queryPageBean);

    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);
}
