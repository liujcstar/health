package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        Jedis resource = jedisPool.getResource();
        //sdiff：将前面集合中的数据减去后面集合中相同的数据，返回值为被减去的数据集合
        Set<String> sdiff = resource.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(sdiff!=null && !sdiff.isEmpty()){
            for (String s : sdiff) {
                QiniuUtils.deleteFileFromQiniu(s);
            }
            resource.del(RedisConstant.SETMEAL_PIC_RESOURCES);
            resource.del(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
            resource.close();
//            System.out.println("我好了。。。");
        }
    }
}
