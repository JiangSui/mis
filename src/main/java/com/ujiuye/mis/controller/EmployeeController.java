package com.ujiuye.mis.controller;

import com.github.pagehelper.PageInfo;
import com.ujiuye.mis.pojo.EmpRole;
import com.ujiuye.mis.pojo.Employee;
import com.ujiuye.mis.qo.EmployeeQuery;
import com.ujiuye.mis.service.EmployeeService;
import com.ujiuye.mis.vo.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("emp")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("{page}/{pageSize}")
    public R findByQuery(@PathVariable Integer page,
                         @PathVariable Integer pageSize,
                         EmployeeQuery employeeQuery){
        List<Employee> eList = employeeService.findByQuery(page,pageSize,employeeQuery);
        return CollectionUtils.isEmpty(eList)?R.error().msg("null"):R.ok().data("pageInfo",new PageInfo<>(eList));
    }

    //保存
    @PostMapping("save/{roleid}")
    public R save(@RequestBody Employee employee,@PathVariable Integer roleid){
        boolean ret = employeeService.save(employee,roleid);
        return ret?R.ok():R.error();
    }

    //获取员工全部信息
    @GetMapping("{eid}")
    public R getEmpByEid(@PathVariable Integer eid){
        Employee e = employeeService.getEmpByEid(eid);
        return ObjectUtils.isEmpty(e)?R.error():R.ok().data("item",e);
    }

    //修改
    @PostMapping("update")
    public R update(@RequestBody Employee employee){
        boolean ret=employeeService.update(employee);
        return ret?R.ok():R.error();
    }

    //删除
    @PostMapping("delete/{eids}")
    public R delete(@PathVariable String eids){
       boolean ret =  employeeService.delete(eids);
       return ret?R.ok():R.error();
    }

    //导出 excel 表格
    @GetMapping("download")
    public ResponseEntity<byte[]> down() throws IOException {
        //获取数据
        List<Employee> eList = employeeService.findAll();

        Workbook workbook = new XSSFWorkbook();

        //创建 工作簿
        Sheet sheet = workbook.createSheet("employee");

        //表头 行
        Row title = sheet.createRow(0);
        //表头单元格信息
        //存数据
        title.createCell(0).setCellValue("编号");
        title.createCell(1).setCellValue("姓名");
        title.createCell(2).setCellValue("性别");
        title.createCell(3).setCellValue("年龄");
        title.createCell(4).setCellValue("电话");
        title.createCell(5).setCellValue("入职日期");
        title.createCell(6).setCellValue("身份证");
        title.createCell(7).setCellValue("用户名");
        title.createCell(8).setCellValue("备注");
        title.createCell(9).setCellValue("部门");

        //遍历数据
        for (Employee e : eList) {
            //表头 行
            //表头 行
            Row row1 = sheet.createRow(sheet.getLastRowNum()+1);
            //表头单元格信息
            //存数据
            row1.createCell(0).setCellValue(e.getEid());
            row1.createCell(1).setCellValue(e.getEname());
            row1.createCell(2).setCellValue(e.getEsex());
            row1.createCell(3).setCellValue(e.getEage());
            row1.createCell(4).setCellValue(e.getTelephone());
            row1.createCell(5).setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(e.getHiredate()));
            row1.createCell(6).setCellValue(e.getPnum());
            row1.createCell(7).setCellValue(e.getUsername());
            row1.createCell(8).setCellValue(e.getRemark());
            row1.createCell(9).setCellValue(e.getDept().getDname());
        }

        //输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        //转成字节数组
        byte[] bytes = byteArrayOutputStream.toByteArray();
        //http头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentDispositionFormData("attchment",new String("员工信息.xlsx".getBytes("utf-8"),"ISO-8859-1"));

        return new ResponseEntity<byte[]>(bytes,httpHeaders, HttpStatus.OK);
    }
}
