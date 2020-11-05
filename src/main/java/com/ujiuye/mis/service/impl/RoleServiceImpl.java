package com.ujiuye.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.ujiuye.mis.mapper.RoleMapper;
import com.ujiuye.mis.mapper.RoleSourcesMapper;
import com.ujiuye.mis.mapper.SourcesMapper;
import com.ujiuye.mis.pojo.*;
import com.ujiuye.mis.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleSourcesMapper roleSourcesMapper;

    @Autowired
    private SourcesMapper sourcesMapper;
    @Override
    public List<Role> selectAll(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        return roleMapper.selectByExample(null);
    }

    @Override
    public List<Sources> getItems() {
        return sourcesMapper.selectByExample(null);
    }

    @Override
    @Transactional //事务管理
    public boolean save(Role role, String ids) {
        try {
            //保存
            //先保存 角色, 再根据角色id 保存 权限
            roleMapper.insertSelective(role);
            Integer roleid = role.getRoleid(); //返回的id
            //拆分 解析roleid
            List<Integer> collect = Arrays.stream(ids.split("-")).map(r -> Integer.valueOf(r)).collect(Collectors.toList());

            //保存权限
            for (Integer id : collect) {
                //添加
                RoleSources rs = new RoleSources();
                rs.setResourcesFk(id);
                rs.setRoleFk(roleid);
                roleSourcesMapper.insert(rs);
            }
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  false;
        }
    }

    //通过id  查找到用户  及其权限
    @Override
    @Transactional
    public Role findById(Integer id) {
        //首先查出 用户 在根据 对应的资源id  查出对应的权限
        Role role = roleMapper.selectByPrimaryKey(id);

        //用户对应的资源 id
        RoleSourcesExample rs = new RoleSourcesExample();
        rs.createCriteria().andRoleFkEqualTo(role.getRoleid());
        List<RoleSources> roleSources = roleSourcesMapper.selectByExample(rs);
        //所有的权限
        List<Sources> sources = sourcesMapper.selectByExample(null);

        //获取用户对应的资源id
        List<Integer> sourceID = roleSources.stream().map(r -> r.getResourcesFk()).collect(Collectors.toList());
        //遍历查询资源 打钩
       for (Sources re:sources){
           if(sourceID.contains(re.getId())){
               //打钩
               re.setChecked(true);
           }
       }
       //将其加入到 role 中
       role.setSourcesList(sources);
        return role;
    }

    //修改
    @Override
    @Transactional
    public boolean update(Role role, String ids) {
        //解析 ids 字符串
        List<Integer> sourceIds = Arrays.stream(ids.split("-")).map(id -> Integer.valueOf(id)).collect(Collectors.toList());
        try {
            //先修改用户表
            roleMapper.updateByPrimaryKeySelective(role);
            //再删除权限对应表,再插入
            RoleSourcesExample rse = new RoleSourcesExample();
            rse.createCriteria().andRoleFkEqualTo(role.getRoleid());
            roleSourcesMapper.deleteByExample(rse);
            //插入
            for (Integer is : sourceIds) {
                RoleSources rs = new RoleSources();
                rs.setRoleFk(role.getRoleid());
                rs.setResourcesFk(is);
                roleSourcesMapper.insertSelective(rs);
            }
            return true;
        }catch (Exception e){
            //手动 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    //删除 通过id
    @Override
    @Transactional
    public boolean delete(String ids) {
        try {
            List<Integer> roleidList = Arrays.stream(ids.split("-")).map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            for (Integer id : roleidList) {
                RoleSourcesExample roleSourcesExample = new RoleSourcesExample();
                roleSourcesExample.createCriteria().andRoleFkEqualTo(id);
                roleSourcesMapper.deleteByExample(roleSourcesExample);

                roleMapper.deleteByPrimaryKey(id);
            }
            return true;
        }catch (Exception e){
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    // 获取所有的角色
    @Override
    public List<Role> getAll() {
        return roleMapper.selectByExample(null);
    }
}
