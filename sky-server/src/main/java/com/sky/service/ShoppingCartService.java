package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import org.springframework.stereotype.Service;


public interface ShoppingCartService {
    /**
     * 添加商品到购物车
     * @param shoppingCartDTO
     */
    void addCart(ShoppingCartDTO shoppingCartDTO);
}
