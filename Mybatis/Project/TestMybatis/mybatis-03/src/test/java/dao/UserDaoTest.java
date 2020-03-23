package dao;

import com.cb414.dao.UserMapper;
import com.cb414.pojo.User;
import com.cb414.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {

    @Test
    public void testGetUserById(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();

        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        User user = userMapper.getUserById(1);

        System.out.println(user);

        sqlSession.close();
    }



}
