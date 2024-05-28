package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 套餐
 */
@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer selectById(Long id);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(value=OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Select("select * from setmeal where id=#{id}")
    Setmeal getById(Long id);

    /**
     * 套餐信息修改
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据id删除套餐
     * @param id
     */
    @Delete("delete from setmeal where id=#{id}")
    void delete(Integer id);
//--------------------------------------------------------------------------------------------------------------------------------
    /**
     * 用户端根据分类id查看套餐信息
     * @param setmeal
     * @return
     */
    List<Setmeal> getSetMealList(Setmeal setmeal);
    @Select("select sd.name,sd.copies,sd.price,d.image,d.description from setmeal_dish as sd left join dish as d on sd.dish_id = d.id where setmeal_id=#{setMealId}")
    List<DishItemVO> getDishItemById(Long setMealId);
}
