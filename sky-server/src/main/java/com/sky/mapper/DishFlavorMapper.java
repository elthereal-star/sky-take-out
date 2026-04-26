package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author SXBai
 * @create 2026-04-14-0:46
 */
@Mapper
public interface DishFlavorMapper {

    void insertBatch(List<DishFlavor> flavorlist);

    @Delete("delete from dish_flavor  where dish_id=#{dishId}")
    void deleteById(Long dishId);

    void deleteByIds(List<Long> dishIds);

    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
