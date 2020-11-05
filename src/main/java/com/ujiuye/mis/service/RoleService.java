package com.ujiuye.mis.service;

import com.ujiuye.mis.pojo.Role;
import com.ujiuye.mis.pojo.Sources;

import java.util.List;
public interface RoleService {

    List<Role> selectAll(Integer page,Integer pageSize);

    List<Sources> getItems();

    boolean save(Role role, String ids);

    Role findById(Integer id);

    boolean update(Role role, String ids);

    boolean delete(String id);

    List<Role> getAll();
}
