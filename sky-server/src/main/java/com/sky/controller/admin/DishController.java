package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜品管理控制类
 */
@RestController
@Slf4j
@Api(tags = "菜品管理接口")
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    /**
     * 新增菜品以及对应的口味
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result saveDish(@RequestBody DishDTO dishDTO){
          log.info("新增菜品：{}",dishDTO);
          dishService.saveDishWithFlavor(dishDTO);
          return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
          log.info("菜品分页查询,{}",dishPageQueryDTO);
          PageResult pageResult=dishService.page(dishPageQueryDTO);
          return Result.success(pageResult);
    }
}
