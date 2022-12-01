package com.dou.acctmanagement.services;

import com.dou.acctmanagement.models.Department;
import com.dou.adm.shared.ResponseObject;

public interface DepartmentServiceInterface {
    ResponseObject getAllDepartment();
    ResponseObject getDepartmentByID(String id);
    ResponseObject insDepartment(Department department);
    ResponseObject updDepartment(Department department);
    ResponseObject delDepartment(Department department);
    ResponseObject getDepartmentByAccountId(String accountId);

}
