package com.sky.service;

import com.sky.dto.DishDTO;
import org.springframework.stereotype.Service;


public interface DishService {

    /**
     * 新增菜品以及对应的口味
     */
    void saveDishWithFlavor(DishDTO dishDTO);
}
