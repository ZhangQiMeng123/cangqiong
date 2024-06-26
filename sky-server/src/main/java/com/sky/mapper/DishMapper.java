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

import java.time.LocalDateTime;
import java.util.List;

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
    Dish getById(Long id);

    /**
     * 菜品信息修改
     * @param dish
     */
    @AutoFill(value=OperationType.INSERT)
    void update(Dish dish);

    /**
     * 批量删除菜品
     * @param id
     */
    @Delete("delete from dish where id=#{id}")
    void deleteByIds(Long id);

    /**
     * 根据菜品分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 用户端根据菜品分类id查看菜品信息
     * @param dish1
     * @return
     */
    List<Dish> getDish(Dish dish1);

    /**
     * 工作台获取菜品数据信息
     * @return
     */
    @Select("select * from dish")
    List<Dish> getDishData();
}
