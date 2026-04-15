package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author SXBai
 * @create 2026-04-15-1:28
 */
@Mapper
public interface SetmealDishMapper {
    List<Long> getSetmealDishIdBySetmealId(List<Long> dishIds);
}
