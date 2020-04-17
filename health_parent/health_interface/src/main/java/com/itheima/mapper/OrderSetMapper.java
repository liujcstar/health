package com.itheima.mapper;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSetMapper {

    List<Map<String,Integer>> findByCurrentMonth(String currentMonth);

    int selectByDate(String orderDate);

    void updateOrderSettingByDate(OrderSetting orderSetting);

    void addOrderSettingByDate(OrderSetting orderSetting);

    long findCountByDate(Date orderDate);

//    void editNumberByDate(OrderSetting orderSetting);
}
