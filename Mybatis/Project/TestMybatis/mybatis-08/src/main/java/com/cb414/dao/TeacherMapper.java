package com.cb414.dao;

import com.cb414.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper {
//    //获取所有老师
//    List<Teacher> getTeacher();


    //获取指定老师下的所有学生
    Teacher getTeacher(@Param("tid") int id);


}
