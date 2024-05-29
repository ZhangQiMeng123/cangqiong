package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "地址相关接口")
@Slf4j
@RequestMapping("/user/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询用的所有地址
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询用户所有地址")
    public Result<List<AddressBook>> getAllBook(){
        log.info("查询用户的所有地址");
        List<AddressBook> addressBookList=addressBookService.getAllBook();
        return Result.success(addressBookList);
    }

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @ApiOperation(value = "地址新增")
    @PostMapping
    public Result saveAddress(@RequestBody AddressBook addressBook){
        log.info("新增地址,{}",addressBook);
        addressBookService.saveAddress(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressById(@PathVariable Long id){
        log.info("根据id查询地址,{}",id);
        AddressBook addressBook=addressBookService.getAddressById(id);
        return Result.success(addressBook);
    }
    @PutMapping
    @ApiOperation(value = "根据id修改地址")
    public Result udateAddressById(@RequestBody AddressBook addressBook){
         log.info("根据id修改地址");
         addressBookService.updateAddress(addressBook);
         return Result.success();
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation(value = "设置默认地址")
    public Result setDefaultAddress(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete(Long id){
        log.info("根据id删除地址,{}",id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 查看默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getAddressByDefault(){
        log.info("查看默认地址");
        AddressBook addressBook=addressBookService.getAddressByDefault();
        return Result.success(addressBook);
    }
}
