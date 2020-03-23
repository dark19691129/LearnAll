package com.cb414.dao;

import com.cb414.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    //根据id查找用户
    User getUserById(int id);

    //分页查询
    List<User> getUserByLimit(Map<String,Object> map);

    //分页查询2
    List<User> getUserByRowBounds();

}
