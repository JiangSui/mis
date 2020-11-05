package com.ujiuye.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.ujiuye.mis.mapper.DeptMapper;
import com.ujiuye.mis.pojo.Dept;
import com.ujiuye.mis.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
    @Override
    public List<Dept> findALl(Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        return deptMapper.selectByExample(null);
    }

    //保存 部门
    @Override
    public boolean save(Dept dept) {
        return deptMapper.insertSelective(dept) > 0;
    }

    //获取所有的部门
    @Override
    public List<Dept> getAll() {
        return deptMapper.selectByExample(null);
    }

    //根据id 查找
    @Override
    public Dept getById(Integer id) {
        return deptMapper.selectByPrimaryKey(id);
    }

    //修改部门信息
    @Override
    public boolean update(Dept dept) {
        return  deptMapper.updateByPrimaryKeySelective(dept) > 0;
    }

    @Override
    public boolean delete(String ids) {
        //将字符串 转为 集合
        List<Integer> collect = Arrays.stream(ids.split("-")).map(r -> Integer.valueOf(r)).collect(Collectors.toList());
        //然后珊瑚部门
        for (Integer integer : collect) {
            deptMapper.deleteByPrimaryKey(integer);
        }
        return true;
    }
}
