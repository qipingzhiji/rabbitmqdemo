package com.rabbitmq.dao;

import com.rabbitmq.entity.Employees;
import java.util.List;

public interface EmployeesMapper {
    int deleteByPrimaryKey(Integer employeeId);

    int insert(Employees record);

    Employees selectByPrimaryKey(Integer employeeId);

    List<Employees> selectAll();

    int updateByPrimaryKey(Employees record);
}