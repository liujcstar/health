package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.DelSetmealPojo;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Transactional
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper mapper;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        mapper.add(setmeal);
        Integer setmealId = setmeal.getId();
        for (Integer checkgroupId : checkgroupIds) {
            mapper.setSetmealAndCheckGroup(setmealId, checkgroupId);
        }
//        添加完成之后将图片名称存进redis的set集合中
        addDBRedis(setmeal.getImg());
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        PageHelper pageHelper = new PageHelper();
        pageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        String queryString = queryPageBean.getQueryString();
        if (queryString != null && queryString.length() > 0) {
            queryString.replaceAll(" ", "");
        }
        Page<Setmeal> page = mapper.findPage(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id删除检查套餐
     *
     * @param
     * @return
     */
    @Override
    public void deleteById(DelSetmealPojo delSetmealPojo) {
        //根据id删除中间关系
        mapper.delAssociationBySetmealId(delSetmealPojo.getId());
        //删除检查套餐
        mapper.delSetmealById(delSetmealPojo.getId());
        //将删除的数据信息存到redis中，删除云端数据
        if (delSetmealPojo.getImg() != null) {
            Jedis resource = jedisPool.getResource();
            resource.sadd(RedisConstant.SETMEAL_PIC_RESOURCES, delSetmealPojo.getImg());
            resource.close();
        }
    }

    /**
     * 根据检查套餐id查询出检查组id
     *
     * @param setmealId
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdBySetmealId(Integer setmealId) {
        return mapper.findCheckgroupIdBySetmealId(setmealId);
    }

    /**
     * 根据id查询检查套餐
     *
     * @param setmealId
     */
    @Override
    public Setmeal findById(Integer setmealId) {
        return mapper.findById(setmealId);
    }

    /**
     * 编辑检查套餐
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //根据id删除所有关联关系
        mapper.delAssociationBySetmealId(setmeal.getId());
        //根据id新增关联关系
        for (Integer checkgroupId : checkgroupIds) {
            mapper.setSetmealAndCheckGroup(setmeal.getId(), checkgroupId);
        }

        //编辑套餐
        mapper.edit(setmeal);
        addDBRedis(setmeal.getImg());
    }


    /**
     * 添加或者修改成功后将图片信息存进redis中
     *
     * @param imgName
     */
    public void addDBRedis(String imgName) {
        if (imgName != null) {
            Jedis resource = jedisPool.getResource();
            resource.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, imgName);
            resource.close();
        }
    }
}

