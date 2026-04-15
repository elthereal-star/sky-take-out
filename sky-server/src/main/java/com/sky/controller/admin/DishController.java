package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SXBai
 * @create 2026-04-13-23:40
 */
@RestController
@RequestMapping("/admin/dish")
@Api("菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    /**
     * 新增菜品
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品");
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }
    /**
     * 对菜品进行分页查询
     */

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("对菜品进行分页查询{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     *  批量删除菜品
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("对菜品批量删除,{}",ids);
        dishService.deleteBeatch(ids);
        return Result.success();
    }
}
