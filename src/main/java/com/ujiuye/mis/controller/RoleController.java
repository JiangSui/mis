package com.ujiuye.mis.controller;

import com.github.pagehelper.PageInfo;
import com.ujiuye.mis.pojo.Role;
import com.ujiuye.mis.pojo.Sources;
import com.ujiuye.mis.service.RoleService;
import com.ujiuye.mis.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    //获取权限列表
    @GetMapping("item")
    public R getItem(){
        List<Sources> ls = roleService.getItems();
        return CollectionUtils.isEmpty(ls)?R.error():R.ok().data("item",ls);
    }


    // 分页查询
    @GetMapping("select/{page}/{pageSize}")
    public R selectAll(@PathVariable Integer page,@PathVariable Integer pageSize){
        //返回
        List<Role> roles = roleService.selectAll(page, pageSize);
        return CollectionUtils.isEmpty(roles)?R.error().msg("获取失败"):R.ok().data("items",new PageInfo<>(roles));
    }


    //保存
    @PostMapping("save/{ids}")
    public R save(@RequestBody Role role,@PathVariable String ids){
        boolean ret = roleService.save(role,ids);
        return ret?R.ok():R.error();
    }

    //获取用户信息
    @GetMapping("{id}")
    public R getRole(@PathVariable Integer id){
        Role r = roleService.findById(id);
        return ObjectUtils.isEmpty(r)?R.error():R.ok().data("item",r);
    }

    //修改 用户信息
    @PostMapping("update/{ids}")
    public R update(@RequestBody Role role,@PathVariable String ids){
        boolean ret = roleService.update(role,ids);
        return ret?R.ok():R.error();
    }

    //删除用户
    @PostMapping("delete/{id}")
    public R delete(@PathVariable String id){
        boolean ret = roleService.delete(id);
        return ret?R.ok():R.error();
    }

    //获取所有的用户信息
    @GetMapping("all")
    public R getAll(){
        List<Role> roles = roleService.getAll();
        return CollectionUtils.isEmpty(roles)?R.error():R.ok().data("item",roles);
    }
}
