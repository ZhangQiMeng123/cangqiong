package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    /**
     * 新增菜品分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 商品分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 商品信息修改
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 商品分类启用禁用
     */
    void updateStatus(Integer status,Long id);

    /**
     * 根据类型选择商品的分类
     *
     * @param type
     * @return
     */
    List<Category> selectByType(Integer type);

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteById(Long id);
//-----------------------------------------------------------------------------------------------------------------------------------------

}
