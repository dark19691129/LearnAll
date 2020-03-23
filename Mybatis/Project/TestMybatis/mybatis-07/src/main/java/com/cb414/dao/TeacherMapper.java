package com.cb414.dao;

import com.cb414.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TeacherMapper {

    @Select("Select * From teacher where id=#{tid}")
    Teacher getTeacher(@Param("tid") int id);
}
