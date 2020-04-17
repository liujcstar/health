package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.OrderSetMapper;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSetService.class)
@Transactional
public class OrderSetServiceImpl implements OrderSetService {

    @Autowired
    private OrderSetMapper mapper;
    /**
     * 根据当前月查询预约情况
     * @param currentMonth
     * @return
     */
    @Override
    public List<Map<String, Integer>> findByCurrentMonth(String currentMonth) {
        return mapper.findByCurrentMonth(currentMonth);
    }

    /**
     * 批量导入
     * @param list
     */
    @Override
    public void upload(List<OrderSetting> list) {
        for (OrderSetting orderSetting : list) {
            Date orderDate = orderSetting.getOrderDate();
            //查询当天是否已经设置了预约
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String format1 = format.format(orderDate);
            int count = mapper.selectByDate(format1);
            if (count > 0){
                mapper.updateOrderSettingByDate(orderSetting);
            }else {
                mapper.addOrderSettingByDate(orderSetting);
            }
        }
    }

    /**
     * 修改预约信息
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = mapper.findCountByDate(orderSetting.getOrderDate());
        if (count > 0) {
            mapper.updateOrderSettingByDate(orderSetting);
        }else{
            mapper.addOrderSettingByDate(orderSetting);
        }
    }
}
