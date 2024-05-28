package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLEngineResult;
import java.util.List;

/**
 * 用户端-菜品信息
 */
@RestController
@Slf4j
@RequestMapping("/user/dish")
@Api(tags = "菜品信息相关接口")
public class DishController_ {
    @Autowired
    private DishService dishService;
    @GetMapping("/list")
    @ApiOperation("根据菜品分类id查询菜品信息")
    public Result<List<DishVO>> getByCategoryId( Long categoryId){
        log.info("根据菜品分类id查询菜品信心,{}",categoryId);
        List<DishVO> dishVOList=dishService.listwithFlavor(categoryId);
        return Result.success(dishVOList);
    }
}
