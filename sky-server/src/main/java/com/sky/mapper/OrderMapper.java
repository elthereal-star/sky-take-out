package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author SXBai
 * @create 2026-04-30-1:52
 */
@Mapper
public interface OrderMapper {
    void insert(Orders orders);
}
