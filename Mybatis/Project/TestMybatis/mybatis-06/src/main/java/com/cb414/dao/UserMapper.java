package com.cb414.dao;


import com.cb414.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("SELECT * FROM mybatis.user")
    List<User> getUsers();


    //根据id查询用户
    //若有多个参数时，必须加@Param
    @Select("SELECT * FROM mybatis.user WHERE id=#{id}")
    User getUserById(@Param("id") int id);
}
