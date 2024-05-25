package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 查询菜品是否关联了套餐
     * @param ids
     * @return
     */
    List<Long> getSetMealIdsByDishIds(List<Integer> ids);

    /**
     * 批量保持菜品与套餐的关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询相关联的菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{id} ")
    List<SetmealDish> getDishBySetmealId(Long id);

    /**
     * 根据套餐id删除与之相关联的菜品
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void deleteBySetMealId(Long id);
}
