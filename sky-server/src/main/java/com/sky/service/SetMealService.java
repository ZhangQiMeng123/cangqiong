package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    /**
     * 新增套餐
     * @param setmealDTO
     */
    void saveSetMeal(SetmealDTO setmealDTO);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 套餐信息修改
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 商品启用禁用
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id批量删除套餐
     * @param ids
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 根据分类id查看套餐信息
     * @param categoryId
     * @return
     */
    List<SetmealVO> getSetmealList(Long categoryId);

    /**
     * 根据套餐id查看套餐包含的菜品信息
     * @param setMealId
     * @return
     */
    List<DishItemVO> getDishItemById(Long setMealId);
}
