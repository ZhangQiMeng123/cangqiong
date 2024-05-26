package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@Slf4j
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
public class ShopController {

    private static final String KEY="SHOP_STATUS";
    //加入操作redis的redisTemplate的对象
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 用户端查看店铺营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "用户端获取店铺的营业状态")
    public Result<String> getStatus_admin(){
        log.info("用户端查看店铺营业状态");
        Integer status= (Integer)redisTemplate.opsForValue().get(KEY);
        return Result.success(status==1?"营业中":"已打烊");
    }
}
