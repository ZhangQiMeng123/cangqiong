package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {

    /**
     * 新增菜品以及对应的口味
     */
    void saveDishWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    DishVO getById(Integer id);

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 商品启用或禁用
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteBatchById(List<Integer> ids);
}
