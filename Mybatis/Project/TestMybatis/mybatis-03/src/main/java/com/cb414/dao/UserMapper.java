package com.cb414.dao;

import com.cb414.pojo.User;

import java.util.List;

public interface UserMapper {

    //根据id查找用户
    User getUserById(int id);

}
