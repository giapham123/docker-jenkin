package com.dou.acctmanagement.services.impl;

import com.dou.acctmanagement.mappers.DepartmentMapper;
import com.dou.acctmanagement.models.Department;
import com.dou.acctmanagement.services.DepartmentServiceInterface;
import com.dou.adm.shared.ResponseObject;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService implements DepartmentServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    private DepartmentMapper departmentMapper;

    public ResponseObject<List<Department>> getAllDepartment( ) {
        List<Department> listDepartment = departmentMapper.getAllDepartment();
        return new ResponseObject(listDepartment);
    }

    public ResponseObject<List<Department>> getDepartmentByID(String id) {
        List<Department> listDepartment = departmentMapper.getDepartmentByID(id);
        return new ResponseObject(listDepartment);
    }

    @Transactional
    public ResponseObject insDepartment(Department Department) {
        try {
            departmentMapper.insDepartment(Department);
            return ResponseObject.INSERT_DATA_SUCCESS;
        }
        catch (Exception e) {
            logger.debug("Error: " + e.toString());
            return ResponseObject.INSERT_DATA_FAIL;
        }
    }

    @Transactional
    public ResponseObject updDepartment(Department Department) {
        try {
            departmentMapper.updDepartment(Department);
            return ResponseObject.UPDATE_DATA_SUCCESS;
        }
        catch (Exception e) {
            logger.debug("Error: " + e.toString());
            return ResponseObject.UPDATE_DATA_FAIL;
        }
    }

    @Transactional
    public ResponseObject delDepartment(Department Department) {
        try {
            departmentMapper.delDepartment(Department);
            return ResponseObject.DELETE_DATA_SUCCESS;
        }
        catch (Exception e) {
            logger.debug("Error: " + e.toString());
            return ResponseObject.DELETE_DATA_FAIL;
        }
    }

    public ResponseObject getDepartmentByAccountId(String accountId) {
        List<Department> listDepartment = departmentMapper.getDepartmentByAccountId(accountId);
        if(listDepartment.size()> 0) {
            if(listDepartment.get(0).getDepartmentID().equals("DM00") || listDepartment.get(0).getDepartmentID() == "DM00") {
                //IT GET ALL DEPARTMENT
                listDepartment = departmentMapper.getAllDepartment();
            }
        }
        return new ResponseObject(listDepartment);
    }
}
