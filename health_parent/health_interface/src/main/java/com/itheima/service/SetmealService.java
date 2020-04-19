package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.DelSetmealPojo;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealService {
    void add(Setmeal setmeal,Integer[] checkgrroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(DelSetmealPojo delSetmealPojo);

    List<Integer> findCheckgroupIdBySetmealId(Integer setmealId);

    Setmeal findById(Integer setmealId);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    Setmeal findAllById(int id);

    List<Setmeal> getSetmeal();
}
