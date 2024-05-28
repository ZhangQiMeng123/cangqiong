package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增菜品以及对应的口味
     */
    @Transactional(rollbackFor = Exception.class, propagation = REQUIRED)
    @Override
    public void saveDishWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //向菜品表中插入数据
        dishMapper.save(dish);
        //向口味表中插入新增菜品时添加的口味
        //首先从菜品表中获取刚刚添加菜品的id
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表中插入n条数据
            dishFlavorMapper.saveBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //开启分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //调用mapper接口去查询数据
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        Long total = page.getTotal();
        List<DishVO> list = page.getResult();
        return new PageResult(total, list);
    }

    /**
     * 根据id查询菜品信息
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        //数据库中查询出来的是Dish型，将Dish型属性赋值给DishVO
        BeanUtils.copyProperties(dish, dishVO);
        //根据菜品id查询菜品对应的口味
        List<DishFlavor> listDishFlavor = dishFlavorMapper.getFlavorById(Long.valueOf(id));
        dishVO.setFlavors(listDishFlavor);
        return dishVO;
    }

    /**
     * 修改菜品信息
     *
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        //向菜品表中提交修改的信息
        dishMapper.update(dish);
        //删除口味表中原有的口味
        dishFlavorMapper.deleteById(dishDTO.getId());
        //向口味表中提交修改的信息
        if (dishFlavors != null && dishFlavors.size() > 0) {
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.saveBatch(dishFlavors);
        }
    }

    /**
     * 商品启用或禁用
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 批量删除菜品 菜品删除要一起删除口味表中的数据，起售中的商品不能删除，关联套餐的菜品不能删除
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatchById(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {  // 起售中的商品不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品是否关联了菜品
        List<Long> list = setMealDishMapper.getSetMealIdsByDishIds(ids);
        if (list != null && list.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品以及对应的口味
        for (Long id : ids) {
            dishMapper.deleteByIds(id);
            //删除口味表中对应的口味
            dishFlavorMapper.deleteById(Long.valueOf(id));
        }
    }

    /**
     * 根据菜品分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        List<Dish> dishList = dishMapper.getByCategoryId(categoryId);
        return dishList;

    }

//-------------------------------------------------------------------------------------------------------------------

    /**
     * 用户根据菜品分类查看菜品信息和口味
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<DishVO> listwithFlavor(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        List<Dish> dishList = dishMapper.getDish(dish);
        ArrayList<DishVO> dishVOArrayList = new ArrayList<>();
        //属性拷贝
        for (Dish dish_ : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish_, dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getFlavorById(dish_.getId());

            dishVO.setFlavors(flavors);
            dishVOArrayList.add(dishVO);
        }
        return dishVOArrayList;
    }
}