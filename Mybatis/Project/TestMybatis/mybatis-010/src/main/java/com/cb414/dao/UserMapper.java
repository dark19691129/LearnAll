package com.cb414.dao;

import com.cb414.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    //根据查询用户
    User getUserById(@Param("id") int id);
}
