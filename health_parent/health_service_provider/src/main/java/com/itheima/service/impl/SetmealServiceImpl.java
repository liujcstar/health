package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.CheckIteMapper;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.DelSetmealPojo;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper mapper;

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private CheckIteMapper checkIteMapper;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String out_put_path;

    @Override
    public void add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        mapper.add(setmeal);
        Integer setmealId = setmeal.getId();
        for (Integer checkgroupId : checkgroupIds) {
            mapper.setSetmealAndCheckGroup(setmealId, checkgroupId);
        }
//        添加完成之后将图片名称存进redis的set集合中
        addDBRedis(setmeal.getImg());
//        添加完成后生成静态界面
        generateMobileStaticHtml();
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
        //修改数据后生成静态界面
        generateMobileStaticHtml();
    }

    /**
     * 查询移动端列表信息
     * @return
     */
    public List<Setmeal> getSetmeal() {
        return this.mapper.getSetmeal();
    }

    /**
     * 查询移动端详细数据
     * @param id
     * @return
     */
    public Setmeal findAllById(int id) {
        Setmeal setmeal = this.mapper.findById(id);
        List<CheckGroup> checkGroups = this.mapper.findCheckgroupBySetmealId(id);
        Iterator var4 = checkGroups.iterator();

        while(var4.hasNext()) {
            CheckGroup checkGroup = (CheckGroup)var4.next();
            List<CheckItem> checkItems = this.checkIteMapper.findCheckItemByCheckGroupId(checkGroup.getId());
            checkGroup.setCheckItems(checkItems);
        }

        setmeal.setCheckGroups(checkGroups);
        return setmeal;
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

    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
        List<Setmeal> setmeal = mapper.getSetmeal();

        //需要生成套餐列表静态页面
        generateMobileSetmealListHtml(setmeal);

        //需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(setmeal);
    }

    //生成详细静态页面信息（多个）
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmeal) {
        for (Setmeal setmeal1 : setmeal) {
            String htmlName = "setmeal_detail_"+ setmeal1.getId() +".html";
            Map map = new HashMap();
            map.put("setmeal", setmeal1);
            generateHtml("mobile_setmeal_detail.ftl",htmlName,map);
        }
    }

    //生成页面列表静态信息
    private void generateMobileSetmealListHtml(List<Setmeal> setmeal) {
        Map map = new HashMap();
        map.put("setmealList", setmeal);
        generateHtml("mobile_setmeal.ftl", "mobile_setmeal.html",map );
    }


    /**
     * 生成静态界面
     */
    public void generateHtml(String templateName,String htmlName,Map map){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            /*
            * 获取相应的模板文件
            * File.separator:获取路径分隔符
            * */
            Template template = configuration.getTemplate(templateName);

            //构造输入流文件
            //File.separator：路径分隔符
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(out_put_path + File.separator + htmlName)),"UTF-8"));

            //输出文件
            template.process(map,out);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (out!=null){
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

