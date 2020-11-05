package com.ujiuye.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.ujiuye.mis.eum.EmployeeConditionQuery;
import com.ujiuye.mis.mapper.DeptMapper;
import com.ujiuye.mis.mapper.EmpRoleMapper;
import com.ujiuye.mis.mapper.EmployeeMapper;
import com.ujiuye.mis.pojo.*;
import com.ujiuye.mis.qo.EmployeeQuery;
import com.ujiuye.mis.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private EmpRoleMapper empRoleMapper;
    @Override
    public List<Employee> findByQuery(Integer page, Integer pageSize, EmployeeQuery employeeQuery) {
        //查询  先判断是按员工查 还是按照 部门查
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();

        //判断1 就是员工姓名
        if(employeeQuery!=null && employeeQuery.getCondition() == EmployeeConditionQuery.EMPLOYEE_ENAME_QUERY.getCondition()){
            criteria.andEnameLike("%"+employeeQuery.getKeywords()+"%");
        }

        //判断2  就是 部门的名字查询
        if(employeeQuery!=null && employeeQuery.getCondition() == EmployeeConditionQuery.EMPLOYEE_DNAME_QUERY.getCondition()){
            //根据部门查询  先查询部门 再根据部门id  找到多个员工
            DeptExample deptExample = new DeptExample();
            deptExample.createCriteria().andDnameLike("%"+employeeQuery.getKeywords()+"%");
            List<Dept> depts = deptMapper.selectByExample(deptExample);

            // 将部门只获取id
            List<Integer> collect = depts.stream().map(r -> r.getDeptno()).collect(Collectors.toList());

            //根据这个部门id 查询员工表
            criteria.andDfkIn(collect);

        }
        //根据部门id 查询 部门

        PageHelper.startPage(page,pageSize);
        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        //每一个 员工类 都需要一个 部门对象

        for (Employee r : employees) {
           r.setDept( deptMapper.selectByPrimaryKey(r.getDfk()));
        }
        return employees;
    }

    //保存角色
    @Override
    @Transactional
    public boolean save(Employee employee, Integer roleid) {
        try {
            //首先保存员工 获取到返回的 eid
            employeeMapper.insertSelective(employee);
            Integer eid = employee.getEid(); //员工eid

            //再保存 员工和 角色的关系表
            EmpRole er = new EmpRole();
            er.setEmpFk(eid);
            er.setRoleFk(roleid);
            empRoleMapper.insertSelective(er);
            return true;
        } catch (Exception e) {
            //手动异常
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    //通过员工id  查找员工信息
    @Override
    public Employee getEmpByEid(Integer eid) {
        //先查找员工
        Employee employee = employeeMapper.selectByPrimaryKey(eid);
        //再根据eid 查找 角色对应的id
        EmpRoleExample empRoleExample = new EmpRoleExample();
        empRoleExample.createCriteria().andEmpFkEqualTo(eid);
        EmpRole empRoles = empRoleMapper.selectByExample(empRoleExample).get(0);


        //将 角色对应的 id 复制进去
        employee.setRoleid(empRoles.getRoleFk());

        return employee;
    }

    //修改
    @Override
    @Transactional
    public boolean update(Employee employee) {
        try {
            //先删除 关系表的 对应行, 再插入
            EmpRoleExample empRoleExample = new EmpRoleExample();
            empRoleExample.createCriteria().andEmpFkEqualTo(employee.getEid());
            empRoleMapper.deleteByExample(empRoleExample);

           //加入
            EmpRole empRole = new EmpRole();
            empRole.setEmpFk(employee.getEid());
            empRole.setErdis("强强IQ那个");
            empRole.setRoleFk(employee.getRoleid());
            empRoleMapper.insertSelective(empRole);

            //修改主表
            employeeMapper.updateByPrimaryKey(employee);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
            e.printStackTrace();
            return  false;
        }
    }

    @Override
    @Transactional
    public boolean delete(String eids) {
        //转成集合
        try {
            List<Integer> collect = Arrays.stream(eids.split("-")).map(r -> Integer.valueOf(r)).collect(Collectors.toList());
            //循环删除
            for (Integer integer : collect) {
                //先删除主表 也就是关系表
                EmpRoleExample empRoleExample = new EmpRoleExample();
                empRoleExample.createCriteria().andEmpFkEqualTo(integer);
                empRoleMapper.deleteByExample(empRoleExample);

                //再删除 从表 员工表
                employeeMapper.deleteByPrimaryKey(integer);
            }
            return true;
        } catch (Exception e) {
            //手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return  false;
        }
    }

    @Override
    public List<Employee> findAll() {
        //封装一个 部门进去
        List<Employee> employees = employeeMapper.selectByExample(null);

        employees.stream().forEach(r->r.setDept(deptMapper.selectByPrimaryKey(r.getDfk())));

        return employees;
    }


}
