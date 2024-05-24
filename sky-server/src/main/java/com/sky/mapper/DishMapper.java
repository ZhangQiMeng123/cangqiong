package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
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

    /**
     * 分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    Dish getById(Integer id);

    /**
     * 菜品信息修改
     * @param dish
     */
    @AutoFill(value=OperationType.INSERT)
    void update(Dish dish);


}
