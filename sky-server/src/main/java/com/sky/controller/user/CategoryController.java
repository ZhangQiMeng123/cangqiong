package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜品分类接口
 */
@RestController
@Slf4j
@Api(tags = "菜品分类相关接口")
@RequestMapping("/user/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品分类查询
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "用户端菜品分类查询")
    @GetMapping("/list")
    public Result<List<Category>> categoryList(Integer type){
          log.info("菜品分类查询：{}",type);
          List<com.sky.entity.Category> categoryList=categoryService.selectByType(type);
          return Result.success(categoryList);
    }
}
