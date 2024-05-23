package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜品分类
 */
@Mapper
public interface CategoryMapper {
    /**
     * 新增菜品分类
     * @param category
     */
    void save(Category category);

    /**
     * 商品分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 商品信息修改
     * @param category
     */
    void update(Category category);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> selectByType(Integer type);

    /**
     * 根据id删除菜品分类
     * @param id
     */
    @Delete("delete from category where id=#{id}")
    void deleteById(Long id);
}
