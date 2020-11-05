package com.ujiuye.mis.controller;

import com.ujiuye.mis.pojo.Employee;
import com.ujiuye.mis.service.LoginService;
import com.ujiuye.mis.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("login")
    public R test1(@RequestBody Employee employee, HttpSession session){

        Employee employee1 = loginService.LoginCheck(employee);

        if(ObjectUtils.isEmpty(employee1)){
            return R.error().msg("用户名或密码不正确,请重新输入");
        }else{
            int active = employee1.getEid();
            session.setAttribute("active",active);
            return R.ok();
        }
    }

    //退出
    @GetMapping("logout")
    public R logout(HttpSession session){
        session.removeAttribute("active");
        return R.ok();
    }
}
