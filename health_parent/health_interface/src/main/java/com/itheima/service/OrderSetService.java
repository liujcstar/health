package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSetService {
    List<Map<String,Integer>> findByCurrentMonth(String currentMonth);

    void upload(List<OrderSetting> list);

    void editNumberByDate(OrderSetting orderSetting);
}
