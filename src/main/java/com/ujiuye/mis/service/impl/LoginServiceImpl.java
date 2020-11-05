package com.ujiuye.mis.service.impl;

import com.ujiuye.mis.mapper.EmployeeMapper;
import com.ujiuye.mis.pojo.Employee;
import com.ujiuye.mis.pojo.EmployeeExample;
import com.ujiuye.mis.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee LoginCheck(Employee employee) {
        EmployeeExample employeeExample = new EmployeeExample();
        employeeExample.createCriteria().andUsernameEqualTo(employee.getUsername()).andPasswordEqualTo(employee.getPassword());
        List<Employee> byUserName = employeeMapper.selectByExample(employeeExample);
        if(CollectionUtils.isEmpty(byUserName)){
            return null;
        }else{
            return byUserName.get(0);
        }
    }
}
