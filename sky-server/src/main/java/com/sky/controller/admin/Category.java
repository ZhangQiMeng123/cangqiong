package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "分类管理接口")
@RequestMapping("/admin/category")
@Slf4j
public class Category {
    @Autowired
    private CategoryService categoryService;
    /**
     * 新增菜品分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品分类")
     public Result saveCategory(@RequestBody CategoryDTO categoryDTO){
         log.info("新增菜品分类,{}",categoryDTO);
         categoryService.save(categoryDTO);
         return Result.success();
     }

    /**
     * 商品分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("商品分页查询")
     public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
           log.info("商品分页查询,{}",categoryPageQueryDTO);
           PageResult pageResult=categoryService.page(categoryPageQueryDTO);
           return Result.success(pageResult);
     }

    /**
     * 商品信息修改
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("商品信息修改")
     public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
         log.info("商品信息修改,{}",categoryDTO);
         categoryService.update(categoryDTO);
         return Result.success();
     }
}
