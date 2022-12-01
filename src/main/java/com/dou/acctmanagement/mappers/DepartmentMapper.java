package com.dou.acctmanagement.mappers;

import com.dou.acctmanagement.models.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<Department> getAllDepartment();
    List<Department> getDepartmentByID(String id);
    void insDepartment(Department department);
    void updDepartment(Department department);
    void delDepartment(Department department);
    List<Department> getDepartmentByAccountId(String accountId);
}
