package com.ujiuye.mis.service;

import com.ujiuye.mis.pojo.Employee;

public interface LoginService {

    //登录验证
    Employee LoginCheck(Employee employee);
}
