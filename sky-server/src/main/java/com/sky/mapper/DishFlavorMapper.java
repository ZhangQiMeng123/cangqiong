package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 口味接口
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     */
    void saveBatch(List<DishFlavor> flavors);

    /**
     * 根据id查询菜品对应的口味
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> getFlavorById(Integer id);

    /**
     * 删除口味表中的数据
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteById(Long id);
}
