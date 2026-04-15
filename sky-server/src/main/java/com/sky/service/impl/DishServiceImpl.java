package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SXBai
 * @create 2026-04-14-0:17
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavorMapper dishFlavorMapper;



    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        /**
         * 向菜品表插入一个数据
         */
        dishMapper.insert(dish);

        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        /**
         *向菜品表插入多个数据
         */
        List<DishFlavor> flavorlist = dishDTO.getFlavors();
        if(flavorlist!=null&&flavorlist.size()>0){
            flavorlist.forEach(dishFlavor->{
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavorlist);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
         PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
         Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //对菜品进行删除操作
    @Override
    @Transactional
    public void deleteBeatch(List<Long> ids) {
        //判断菜品是否在售
        for(Long id:ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        for(Long id:ids){
            dishMapper.deletById(id);
            dishFlavorMapper.deleteById(id);
        }

    }


}
