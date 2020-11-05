package com.ujiuye.mis.service;

import com.ujiuye.mis.pojo.Sources;

import java.util.List;

public interface SourceService {

    //查找 所有的节点
    Sources findAll();

    List<Sources> findItem();

    boolean save(Sources sources);

    Sources findById(Integer id);

    boolean update(Sources sources);

    boolean deleteById(Integer id);

    List<Sources> getSourceById(Integer id);
}
