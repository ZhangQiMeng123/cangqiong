package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void saveSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        //获取生成的套餐id
        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        //保存套餐和菜品的关联关系
        setMealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page=setmealMapper.page(setmealPageQueryDTO);
        Long total=page.getTotal();
        List<SetmealVO> list=page.getResult();
        return new PageResult(total,list);
    }


    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        //从setmeal表中查询基础数据
        Setmeal setmeal=setmealMapper.getById(id);
        //从setmeal_dish表中查询套餐关联的菜品
        List<SetmealDish> setmealDishes=setMealDishMapper.getDishBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 套餐信息修改
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //如果需要修改菜品，需要获得套餐对应的id
        Long id=setmeal.getId();
        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        //删除与套餐相关联的菜品
        setMealDishMapper.deleteBySetMealId(id);
        //重新插入与套餐相关联的菜品
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(id);
        });
        //3、重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
        setMealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 商品启用禁用
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 根据id批量删除套餐
     * @param ids
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        for(Integer id:ids){
            Setmeal setmeal = setmealMapper.getById(Long.valueOf(id));
            if(setmeal.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);  //套餐正在售卖中，不能删除
            }
            setmealMapper.delete(id);
            setMealDishMapper.deleteBySetMealId(Long.valueOf(id));
        }
    }

//    --------------------------------------------------------------------------------------------------------------------------------

    /**
     * 根据分类id查看套餐信息
     * @param categoryId
     * @return
     */
    @Override
    public List<SetmealVO> getSetmealList(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE); List<Setmeal> setmealList=setmealMapper.getSetMealList(setmeal);
        ArrayList<SetmealVO> setmealVOS = new ArrayList<>();
        SetmealVO setmealVO = new SetmealVO();
        if(setmealList!=null) {
            for (Setmeal setmeal1 : setmealList) {
                BeanUtils.copyProperties(setmeal1,setmealVO);
                setmealVOS.add(setmealVO);
            }
        }
        return setmealVOS;

    }

    /**
     * 根据套餐id查看套餐包含的菜品信息
     * @param setMealId
     * @return
     */
    @Override
    public List<DishItemVO> getDishItemById(Long setMealId) {
        List<DishItemVO> dishItemVOList=setmealMapper.getDishItemById(setMealId);
        return dishItemVOList;
    }
}
