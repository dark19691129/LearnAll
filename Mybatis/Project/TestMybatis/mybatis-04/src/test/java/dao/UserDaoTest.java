package dao;

import com.cb414.dao.UserMapper;
import com.cb414.pojo.User;
import com.cb414.utils.MybatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoTest {

    static Logger logger=Logger.getLogger(UserDaoTest.class);

    @Test
    public void testGetUserByLimit(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Map map=new HashMap<String,Integer>();
        map.put("startIndex",0);
        map.put("pageSize",2);
        List<User> userByLimit = mapper.getUserByLimit(map);

        for(User user:userByLimit){
            System.out.println(user);
        }

        sqlSession.close();
    }

    @Test
    public void testGetUserByRowBounds(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();

        RowBounds rowBounds = new RowBounds(1, 2);


        List<User> userList = sqlSession.selectList("com.cb414.dao.UserMapper.getUserByRowBounds",null,rowBounds);

        for (User user:userList){
            System.out.println(user);
        }

        sqlSession.close();
    }

    @Test
    public void testGetUserById(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();

        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        User user = userMapper.getUserById(1);

        System.out.println(user);

        sqlSession.close();
    }


    @Test
    public void testLog4j(){
        logger.info("info:这是一个info信息");
        logger.debug("debug:这是一个debug信息");
        logger.error("error:这是一个error信息");
    }

}
