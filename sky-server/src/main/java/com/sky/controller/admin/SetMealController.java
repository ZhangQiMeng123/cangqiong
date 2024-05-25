package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;

import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐控制接口
 */
@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增套餐")
    public Result saveSetMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐,{}",setmealDTO);
        setMealService.saveSetMeal(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @ApiOperation(value = "套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询,{}",setmealPageQueryDTO);
        PageResult pageResult=setMealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询套餐信息")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id){
         log.info("根据id查询套餐信息,{}",id);
         SetmealVO setmealVo=setMealService.getById(id);
         return Result.success(setmealVo);
    }
    /**
     * 套餐信息修改
     * @param setmealDTO
     * @return
     */
    @ApiOperation(value = "套餐信息修改")
    @PutMapping
    public Result updateSetMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("套餐信息修改,{}",setmealDTO);
        setMealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐启用禁用
     * @param status
     * @param id
     * @return
     */
    @ApiOperation(value = "套餐启用禁用")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("套餐启用禁用,{},{}",status,id);
        setMealService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据id批量删除套餐
     * @param ids
     * @return
     */
    @ApiOperation(value = "根据id批量删除套餐")
    @DeleteMapping
    public Result deleteByIds(@RequestParam List<Integer> ids){
          log.info("根据id批量删除套餐,{}",ids);
          setMealService.deleteByIds(ids);
          return Result.success();
    }
}
