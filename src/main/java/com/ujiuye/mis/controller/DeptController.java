package com.ujiuye.mis.controller;

import com.github.pagehelper.PageInfo;
import com.ujiuye.mis.pojo.Dept;
import com.ujiuye.mis.service.DeptService;
import com.ujiuye.mis.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    //分页查询
    @GetMapping("{page}/{pageSize}")
    public R findAll(@PathVariable Integer page,@PathVariable Integer pageSize){
        List<Dept> aLl = deptService.findALl(page,pageSize);
        return CollectionUtils.isEmpty(aLl)?R.error():R.ok().data("items",new PageInfo<>(aLl));
    }

    //保存部门
    @PostMapping("save")
    public R save(@RequestBody Dept dept){
        boolean ret = deptService.save(dept);
        return ret?R.ok():R.error();
    }

    // 所有的部门信息
    @GetMapping("item")
    public R getAll(){
        List<Dept> all = deptService.getAll();
        return CollectionUtils.isEmpty(all)?R.error():R.ok().data("item",all);
    }

    //根据部门的id 查询部门信息
    @GetMapping("{id}")
    public R getById(@PathVariable Integer id){
        Dept dept = deptService.getById(id);
        return ObjectUtils.isEmpty(dept)?R.error():R.ok().data("item",dept);
    }

    //修改
    @PostMapping("update")
    public R update(@RequestBody Dept dept){
        boolean ret = deptService.update(dept);
        return  ret?R.ok():R.error();
    }

    @PostMapping("{ids}")
    public  R delete(@PathVariable String ids){
        boolean ret = deptService.delete(ids);
        return ret?R.ok():R.error();
    }
}
