package com.ujiuye.mis.service;

import com.ujiuye.mis.pojo.Employee;
import com.ujiuye.mis.qo.EmployeeQuery;

import java.util.List;

public interface EmployeeService {
    List<Employee> findByQuery(Integer page, Integer pageSize, EmployeeQuery employeeQuery);

    boolean save(Employee employee, Integer roleid);

    Employee getEmpByEid(Integer eid);

    boolean update(Employee employee);

    boolean delete(String eids);

    List<Employee> findAll();
}
