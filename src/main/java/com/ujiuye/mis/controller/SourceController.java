package com.ujiuye.mis.controller;

import com.ujiuye.mis.pojo.Sources;
import com.ujiuye.mis.service.SourceService;
import com.ujiuye.mis.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("source")
public class SourceController {

    @Autowired
    private SourceService sourceService;

    @RequestMapping("list")
    public R tesst1(){
        Sources all = sourceService.findAll();
        //判断是否为空
        return ObjectUtils.isEmpty(all)? R.error().msg("查询失败"):R.ok().data("item",all);
    }

    //下拉列表
    @RequestMapping("item")
    public R test2(){
        List<Sources> sList = sourceService.findItem();
        return ObjectUtils.isEmpty(sList)?R.error().msg("获取失败"):R.ok().data("item",sList);
    }


    @RequestMapping("save")
    public R test3(@RequestBody Sources sources){
        boolean ret = sourceService.save(sources);
        return ret?R.ok():R.error().msg("保存失败");
    }

    //修改前的查询
    @GetMapping("{id}")
    public R test4(@PathVariable Integer id){
        Sources ret = sourceService.findById(id);
        return ObjectUtils.isEmpty(ret)?R.error().msg("失败"):R.ok().data("item",ret);
    }

    //修改
    @PostMapping("update")
    public R test5(@RequestBody Sources sources){
        boolean ret = sourceService.update(sources);
        return ret?R.ok():R.error().msg("修改失败");
    }

    //删除
    @PostMapping("{id}")
    public R test6(@PathVariable Integer id){
        boolean ret = sourceService.deleteById(id);
        return ret?R.ok():R.error();
    }

    //获取权限 列表source/source/1
    @GetMapping("own")
    public R getSource(HttpSession httpSession){
        //获取到存在session 里面的eid
        Object active_ = httpSession.getAttribute("active");

        int active = (int)active_;
        //根据用户的id  查出权限列表 并返回
        List<Sources> lSouces = sourceService.getSourceById(active);

        return CollectionUtils.isEmpty(lSouces)?R.error().msg("null"):R.ok().data("item",lSouces);
    }
}
