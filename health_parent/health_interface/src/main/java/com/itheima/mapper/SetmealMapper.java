package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealMapper {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    Page<Setmeal> findPage(String queryString);

    void delAssociationBySetmealId(Integer id);

    void delSetmealById(Integer id);

    List<Integer> findCheckgroupIdBySetmealId(Integer setmealId);

    Setmeal findById(Integer setmealId);

    void edit(Setmeal setmeal);

    List<CheckGroup> findCheckgroupBySetmealId(int id);

    List<Setmeal> getSetmeal();
}
