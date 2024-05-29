package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 查询用户的所有地址
     */
    List<AddressBook> getAllBook();

    /**
     * 新增地址
     * @param addressBook
     */
    void saveAddress(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook getAddressById(Long id);

    /**
     * 根据id修改地址
     */
    void updateAddress(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);

    /**
     * 根据id删除地址
     */
    void deleteById(Long id);

    /**
     * 查看默认地址
     * @return
     */
    AddressBook getAddressByDefault();
}
