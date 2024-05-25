package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品信息")
    public Result<DishVO> getById(@PathVariable Integer id){
        log.info("根据id查询菜品信息,{}",id);
        DishVO dishVO=dishService.getById(id);
        return Result.success(dishVO);
    }
    /**
     * 菜品信息修改
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "菜品信息修改")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息,{}",dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 商品起售或禁用
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "菜品起售或禁用")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("商品启用或禁用,{},{}",status,id);
        dishService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除菜品")
    public Result deleteByIds(@RequestParam List<Integer> ids){
         log.info("批量删除菜品,{}",ids);
         dishService.deleteBatchById(ids);
         return Result.success();
    }

    /**
     * 根据菜品分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据菜品分类id查询菜品")
    public Result<List<Dish>> getDishByCategoryId(Long categoryId){
        log.info("根据菜品分类id查询菜品");
        List<Dish> list= dishService.getByCategoryId(categoryId);
        return Result.success(list);
    }
}
