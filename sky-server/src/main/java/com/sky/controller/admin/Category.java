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

import java.util.List;

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

    /**
     * 菜品分类启用禁用
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "商品分类启用禁用")
     public Result startOrStop(@PathVariable Integer status,Long id){
         log.info("菜品分类启用禁用,{}",status);
         categoryService.updateStatus(status,id);
         return Result.success();
     }
     @GetMapping("/list")
     @ApiOperation("根据类型查询分类")
     public List<com.sky.entity.Category> selectByType(Integer type){
           log.info("根据类型查询分类,{}",type);
           List<com.sky.entity.Category> list= categoryService.selectByType(type);
           return list;
     }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result deleteById(Long id){
        log.info("根据id删除分类,{}",id);
        categoryService.deleteById(id);
        return Result.success();
    }
}
