package com.dou.acctmanagement.controllers;

import com.dou.acctmanagement.models.Department;
import com.dou.acctmanagement.services.DepartmentServiceInterface;
import com.dou.adm.security.JwtUser;
import com.dou.adm.shared.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dou.adm.security.JwtAuthFilter.REQ_USR;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentServiceInterface departmentServiceInterface;

    @RequestMapping("/getByID/{searchVal}")
    public ResponseObject<List<Department>> getDepartmentByID(@PathVariable("searchVal") String searchVal ) {
        return departmentServiceInterface.getDepartmentByID(searchVal);
    }

    @RequestMapping("/getAll")
    public ResponseObject<List<Department>> getAllDepartment()
    {
        return departmentServiceInterface.getAllDepartment();
    }


    @PostMapping("/ins")
    public ResponseObject insDepartment(@RequestBody Department department)  {
        return departmentServiceInterface.insDepartment(department);
    }

    @PostMapping("/upd")
    public ResponseObject updDepartment(@RequestBody Department department)  {
        return departmentServiceInterface.updDepartment(department);
    }

    @PostMapping("/del")
    public ResponseObject delDepartment(@RequestBody Department department)  {
        return departmentServiceInterface.delDepartment(department);
    }

    @GetMapping("/getDepartmentNameByAccountId")
    public ResponseObject<List<Department>> getDepartmentByAccountId(@RequestAttribute(value = REQ_USR, required = false) JwtUser user) {
        return departmentServiceInterface.getDepartmentByAccountId(user.getUsername());
    }
}
