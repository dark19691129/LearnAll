import com.cb414.dao.UserMapper;
import com.cb414.pojo.User;
import com.cb414.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class MyTest {


    @Test
    public void test(){
        /**
         * 从打开sqksession开始，到关闭sqlsession，这段周期就叫做一级缓存
         */
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        User user = mapper.getUserById(1);
        System.out.println(user);

        System.out.println("===============华丽的分割线================");


        User user2 = mapper.getUserById(1);
        System.out.println(user2);
        System.out.println(user==user2);

        sqlSession.close();
    }


    /**
     * 测试二级缓存
     */
    @Test
    public void test2(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User userById = mapper.getUserById(1);
        System.out.println(userById);
        sqlSession.close();


        System.out.println("=======================超级华丽的分割线===============================");

        SqlSession sqlSession2=MybatisUtils.getSqlSession();
        UserMapper mapper1 = sqlSession2.getMapper(UserMapper.class);
        User userById1 = mapper1.getUserById(1);
        System.out.println(userById1);
        sqlSession2.close();


        System.out.println(userById==userById1);

    }

}
