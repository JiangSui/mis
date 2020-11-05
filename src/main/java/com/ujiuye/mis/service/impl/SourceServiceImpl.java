package com.ujiuye.mis.service.impl;

import com.ujiuye.mis.mapper.EmpRoleMapper;
import com.ujiuye.mis.mapper.RoleSourcesMapper;
import com.ujiuye.mis.mapper.SourcesMapper;
import com.ujiuye.mis.pojo.*;
import com.ujiuye.mis.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    private SourcesMapper sourcesMapper;

    @Autowired
    private EmpRoleMapper empRoleMapper;

    @Autowired
    private RoleSourcesMapper roleSourcesMapper;
    @Override
    public Sources findAll() {
        SourcesExample sourcesExample = new SourcesExample();
        sourcesExample.createCriteria().andPidEqualTo(0); //这是根节点
        //根节点
        Sources root = sourcesMapper.selectByExample(sourcesExample).get(0);

        //根据根节点的 id 当做pid  查询子节点 递归    //将该节点加入到 sources 里面
        setSources(root);

        return root;
    }

    //获取下拉列表  ,就会 pid 等于1 的资源
    @Override
    public List<Sources> findItem() {
        SourcesExample sourcesExample = new SourcesExample();
        sourcesExample.createCriteria().andPidEqualTo(1);
        return sourcesMapper.selectByExample(sourcesExample);
    }


    //保存
    @Override
    public boolean save(Sources sources) {
        return sourcesMapper.insertSelective(sources)>0;
    }


    //实现查找当前id的数据
    @Override
    public Sources findById(Integer id) {
        return sourcesMapper.selectByPrimaryKey(id);
    }


    //修改
    @Override
    public boolean update(Sources sources) {
        return sourcesMapper.updateByPrimaryKeySelective(sources) > 0;
    }

    //删除
    @Override
    public boolean deleteById(Integer id) {
        return sourcesMapper.deleteByPrimaryKey(id) > 0;
    }

    //通过员工的id 查询到 权限
    @Override
    public List<Sources> getSourceById(Integer id) {
        //先查询出 员工对应的 角色id
        EmpRoleExample empRoleExample = new EmpRoleExample();
        empRoleExample.createCriteria().andEmpFkEqualTo(id);

        EmpRole empRoles = empRoleMapper.selectByExample(empRoleExample).get(0);
        //获取到角色的id  再根据 角色id  查出权限id
        RoleSourcesExample roleSourcesExample = new RoleSourcesExample();
        roleSourcesExample.createCriteria().andRoleFkEqualTo(empRoles.getRoleFk());
        //权限id 查出来了
        List<RoleSources> roleSources = roleSourcesMapper.selectByExample(roleSourcesExample);

        List<Integer> sourceIds = new ArrayList<>();
        for (RoleSources roleSource : roleSources) {
            sourceIds.add(roleSource.getResourcesFk());
        }
        //根据权限id  查出权限列表
        SourcesExample sourcesExample = new SourcesExample();
        sourcesExample.createCriteria().andIdIn(sourceIds);
        //资源列表
        List<Sources> lSouces = sourcesMapper.selectByExample(sourcesExample);
        //返回


        //对 返回的权限进行筛选 根目录 一级  二级
        Sources root = null;
        for (Sources lSouce : lSouces) {
            if(lSouce.getPid()==0){
                root = lSouce;
            }
        }
        //二级  根据root的id  查找
        List<Sources> sons = new ArrayList<>();
        for (Sources lSouce : lSouces) {
            if(lSouce.getPid()==root.getId()){
                sons.add(lSouce);
            }
        }

        //三级
        for (Sources son : sons) {
            for (Sources lSouce : lSouces) {
                if(son.getId() == lSouce.getPid()){
                    son.getChildren().add(lSouce);
                }
            }
        }
        //返回二级
        return sons;
    }


    private void setSources(Sources root) {
        SourcesExample sourcesExample2 = new SourcesExample();
        sourcesExample2.createCriteria().andPidEqualTo(root.getId()); //再次做根节点
        List<Sources> sources = sourcesMapper.selectByExample(sourcesExample2); //查他的子节点
        if(sources!=null && sources.size()>0){
            //就保存到root里面
            root.setChildren(sources);
            //查他的子节点
            /*for (Sources r:sources) {
                setSources(r);
            }*/
            //流式编程
            sources.stream().forEach(s->setSources(s));
        }
    }
}
