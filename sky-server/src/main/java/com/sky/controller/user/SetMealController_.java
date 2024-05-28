package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户端-套餐相关接口
 */
@RestController
@Slf4j
@ApiOperation(value = "用户端套餐相关接口")
@RequestMapping("/user/setmeal")
public class SetMealController_ {
    @Autowired
    private SetMealService setMealService;
    @GetMapping("/list")
    @ApiOperation(value = "用户端根据分类id查看套餐")
    public Result<List<SetmealVO>> getBySetMeal(Long categoryId){
        log.info("用户端查看套餐接口,{}",categoryId);
        List<SetmealVO> setmealVOS=setMealService.getSetmealList(categoryId);
        return Result.success(setmealVOS);
    }

    /**
     * 根据套餐id查看套餐包含的菜品信息
     * @param setMealId
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation(value = "根据套餐id查看套餐包含的菜品信息")
    public Result<List<DishItemVO>> getSetMealDish(@PathVariable("id") Long setMealId){
        log.info("根据套餐id查看套餐包含的菜品信息,{}",setMealId);
        List<DishItemVO> dishItemVOList=setMealService.getDishItemById(setMealId);
        return Result.success(dishItemVOList);
    }
}
