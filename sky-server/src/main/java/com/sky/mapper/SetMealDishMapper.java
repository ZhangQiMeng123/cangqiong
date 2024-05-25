package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    /**
     * 查询菜品是否关联了套餐
     * @param ids
     * @return
     */
    List<Long> getSetMealIdsByDishIds(List<Integer> ids);
}
