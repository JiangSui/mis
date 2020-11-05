package com.ujiuye.mis.service;

import com.ujiuye.mis.pojo.Dept;
import java.util.List;
public interface DeptService {
    //查找所有
    List<Dept> findALl(Integer page,Integer pageSize);

    boolean save(Dept dept);

    List<Dept> getAll();

    Dept getById(Integer id);

    boolean update(Dept dept);

    boolean delete(String ids);
}
