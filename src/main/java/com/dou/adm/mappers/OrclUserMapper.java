package com.dou.adm.mappers;

import com.dou.adm.configuration.TargetDataSource;
import com.dou.adm.models.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Tu.Tran on 10/1/2018.
 */

@Mapper
public interface OrclUserMapper {
    public List<User> findAllUserSlave();
}
