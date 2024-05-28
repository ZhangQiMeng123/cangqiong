package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 添加商品到购物车
 */
@Api(tags = "购物车相关接口")
@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加商品到购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加商品到购物车:{}",shoppingCartDTO);
        shoppingCartService.addCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return
     */
    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listResult(){
        log.info("查看购物车");
        List<ShoppingCart> shoppingCarts=shoppingCartService.list();
        return Result.success(shoppingCarts);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result cleanCard(){
        log.info("清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }

    /**
     * 删除购物车中一个商品
     * @return
     */
    @PostMapping("/sub")
    public Result delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中一个商品");
        shoppingCartService.delete(shoppingCartDTO);
        return Result.success();
    }
}
