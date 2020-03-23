package com.cb414.dao;

import com.cb414.pojo.User;
import com.cb414.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoTest {

//模糊查询
    @Test
    public void testGetUserLike(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        List<User> userList=mapper.getUserLike("李");

        for(User user:userList){
            System.out.println(user);
        }

        System.out.println("============================");
        sqlSession.close();
    }


    @Test
    public void test(){
        //获取sqlsession对象
        SqlSession sqlSession=MybatisUtils.getSqlSession();

        //方式一：（方式二开始过时了，官方建议使用方式一）
        //执行
        //面向接口编程，UserDao和UserMapper只是一个接口和实现类的关系
        //通过获取UserDao的类，从而获取这个接口
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        //在调用这个接口的方法
        List<User> userList=userMapper.getUserList();


//        //方式二
//        List<User> userList = sqlSession.selectList("com.cb414.dao.UserDao.getUserList");

        for(User user:userList){
            System.out.println(user);
        }
    }

    @Test
    public void testGetUserById(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();

        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        User user = userMapper.getUserById(1);

        System.out.println(user);

        sqlSession.close();
    }


    //注意：增删改需要提交事务才会生效！！！！不然数据库是不会增加的(可以正常运行，但不会生效在数据库)
    @Test
    public void testAddUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.addUser(new User(4,"哈哈","111222"));

        //提交事务
        sqlSession.commit();
        sqlSession.close();

    }


    //更新用户
    @Test
    public void testUpdateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUser(new User(4,"呵呵","123123"));
        //提交事务
        sqlSession.commit();

        sqlSession.close();
    }

    //删除用户
    @Test
    public void testDeleteUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteUser(4);
        sqlSession.commit();

        sqlSession.close();

    }

//    @Test
//    public void testAddUser2(){
//        SqlSession sqlSession=MybatisUtils.getSqlSession();
//        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        Map map=new HashMap<String,Object>();
//        map.put("userId",5);
//        map.put("password","123111");
//
//        mapper.addUser2(map);
//
//
//        //sqlSession.commit();
//
//        sqlSession.close();
//
//    }
}
