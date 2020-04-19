package com.itheima.mapper;

import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckIteMapper {
    void add(CheckItem checkItem);

    List<CheckItem> findPage(String queryString);

    CheckItem findById(int id);

    void edit(CheckItem checkItem);

    void deleteById(int id);

    long findByCheckItemId(int id);

    void deleteByCheckItemId(int id);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemByCheckGroupId(Integer id);
}
