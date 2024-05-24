package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 菜品
 */
@Mapper
public interface DishMapper {
    /**
     * 查看该id对象的菜品分类是否关联了正在售卖中的菜品
     * @param id
     * @return
     */
    @Select("select count(id) from dish where category_id=#{categoryId}")
    Integer seleteById(Long id);

    /**
     * 菜品新增
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void save(Dish dish);
}
