package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void saveEmployee(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageSelect(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 员工账号启用或停用
     * @param status
     */
    void startOrstop(Integer status,Long id);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 更改员工信息
     * @param employeeDTO
     */
    void updateEmployeeInfo(EmployeeDTO employeeDTO);

    /**
     * 密码修改
     */
    void updatePassword(PasswordEditDTO passwordEditDTO);
}
