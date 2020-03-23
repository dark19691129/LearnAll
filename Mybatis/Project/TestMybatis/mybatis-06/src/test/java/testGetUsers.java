import com.cb414.dao.UserMapper;
import com.cb414.pojo.User;
import com.cb414.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class testGetUsers {


    @Test
    public void test(){
        SqlSession sqlSession= MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        List<User> users = mapper.getUsers();
//
//        for (User user : users) {
//            System.out.println(user);
//        }

        User userById = mapper.getUserById(1);

        System.out.println(userById);
        sqlSession.close();
    }
}
