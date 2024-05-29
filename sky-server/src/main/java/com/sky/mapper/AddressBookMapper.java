package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    /**
     * 查询用户的所有地址
     */
    @Select("select * from address_book where user_id=#{userId}")
    List<AddressBook> getAll(Long userId);

    /**
     * 新增地址
     * @param addressBook
     */
    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label) " +
            "value" +
            "(#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label})")
    void save(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Select("select * from address_book where id=#{id}")
    AddressBook getAddressById(Long id);

    /**
     * 根据id修改地址
     * @param addressBook
     */
    void updateAddress(AddressBook addressBook);

    /**
     * 将所有地址设为非默认地址
     * @param addressBook
     */
    @Update("update address_book set is_default =#{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 将当前地址设为默认地址
     * @param addressBook
     */
    @Update("update address_book set is_default=#{isDefault} where id=#{id}")
    void update(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     */
    @Delete("delete from address_book where id=#{id}")
    void deleteById(Long id);
}
