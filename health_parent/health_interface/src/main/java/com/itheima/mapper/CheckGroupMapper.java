package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupMapper {
    Page<CheckGroup> findPage(String queryString);

    Long findTotal();

    void addCheckGroup(CheckGroup checkGroup);

    void addByCheckItemId(@Param("checkgroupId") Integer id, @Param("checkitemId")Integer checkitemId);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdByCheckGroupId(Integer id);

    void updateCheckgroup(CheckGroup checkGroup);

    void deleteAssociation(Integer checkgroupId);
}
