package com.dou.staff_info.services;


import com.dou.adm.shared.ResponseObject;

public interface AccountInfoBussinessServiceInterface {
    ResponseObject getAll(String supervisorId);
}
