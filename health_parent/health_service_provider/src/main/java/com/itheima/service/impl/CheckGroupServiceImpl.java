package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.CheckGroupMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper mapper;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        PageHelper pageHelper = new PageHelper();
        pageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        String queryString = queryPageBean.getQueryString();

        if (queryString!=null && queryString.length()>0) {
            queryString = queryPageBean.getQueryString().replaceAll(" ", "");
        }
        Page<CheckGroup> page = mapper.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
//        添加检查组并返回id
        mapper.addCheckGroup(checkGroup);
//        添加关联信息
        this.setAssociation(checkGroup.getId(),checkitemIds);
    }

    /**
     * 根据id查询检查组信息
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return mapper.findById(id);
    }

    /**
     * 根据检查组id查询检查项
     * @param id
     * @return
     */

    @Override
    public List<Integer> findCheckItemIdByCheckGroupId(Integer id) {
        return mapper.findCheckItemIdByCheckGroupId(id);
    }

    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //跟新检查组信息
        mapper.updateCheckgroup(checkGroup);

        //删除所有关联关系
        Integer checkgroupId = checkGroup.getId();

        mapper.deleteAssociation(checkgroupId);
        //添加关联关系
        this.setAssociation(checkgroupId,checkitemIds);
    }

    /**
     * 根据id删除检查组
     * @param id
     */
    @Override
    public void deleteById(int id) {
        //删除关联关系
        mapper.deleteAssociation(id);
        //删除检查组
        mapper.deleteCheckgroup(id);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return  mapper.findAll();
    }

    /**
     * 添加关联关系
     * @param checkgroupId
     * @param checkitemIds
     */
    public void setAssociation(Integer checkgroupId, Integer[] checkitemIds) {
        for (Integer checkitemId : checkitemIds) {
            mapper.addByCheckItemId(checkgroupId,checkitemId);
        }
    }
}
