package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 查看购物车是否含有该商品
     * @param shoppingCart
     * @return
     */

    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 购物车中已有当前商品，将当前商品的数量在原有的数量基础上加一
     * @param shoppingCart
     */
    @Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 购物车中还未有当前商品，向购车中添加商品
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 查看购物车
     * @return
     */
    @Select("select * from shopping_cart")
    List<ShoppingCart> getCards();

    /**
     * 清空购物车
     */
    @Delete("delete from shopping_cart where user_id=#{id}")
    void clean(Long id);

    void deleteById(Long id);
}
