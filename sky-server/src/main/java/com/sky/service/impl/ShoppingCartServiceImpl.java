package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 添加商品到购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //判断商品是否在购物车中
        List<ShoppingCart> cartList=shoppingCartMapper.list(shoppingCart);
        if(cartList!=null && cartList.size()==1){
            //当前商品已经存在，则该商品数量加1
            shoppingCart=cartList.get(0);  //获取列表中的当前商品
            shoppingCart.setNumber(shoppingCart.getNumber()+1); //在该商品原有数量的基础上加一
            shoppingCartMapper.updateNumberById(shoppingCart);
        }else {
            //当前商品不在购物车中，就将当前商品加入到购物车，并将当前商品的数量设置为1
            //判断当前要添加到购物车的是菜品还是套餐
            Long dishId=shoppingCart.getDishId();
            if (dishId!=null){
                //当前添加到购物车的是菜品
               Dish dish=dishMapper.getById(dishId);
               shoppingCart.setImage(dish.getImage());
               shoppingCart.setAmount(dish.getPrice());
               shoppingCart.setName(dish.getName());
            }else{
                    //当前加入购物车的是套餐
                    Setmeal setmeal=setmealMapper.getById(shoppingCart.getSetmealId());
                    shoppingCart.setName(setmeal.getName());
                    shoppingCart.setAmount(setmeal.getPrice());
                    shoppingCart.setImage(setmeal.getImage());

            }
            shoppingCart.setNumber(1); //将当前商品数量设置为1
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        List<ShoppingCart> shoppingCarts=shoppingCartMapper.getCards();
        return shoppingCarts;
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        shoppingCartMapper.clean(BaseContext.getCurrentId());
    }

    /**
     * 删除购物车中的一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list!=null && list.size()>0){
            shoppingCart=list.get(0);
            Integer num=shoppingCart.getNumber();
            if(num==1){
                //当前商品在购物车中只有一份，直接删除当前记录
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else{
                shoppingCart.setNumber(shoppingCart.getNumber()-1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }

        }
    }

}
