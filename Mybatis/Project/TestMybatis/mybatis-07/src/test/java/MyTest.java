import com.cb414.dao.StudentMapper;
import com.cb414.dao.TeacherMapper;
import com.cb414.pojo.Student;
import com.cb414.pojo.Teacher;
import com.cb414.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyTest {
    @Test
    public void test(){
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);

        Teacher teacher = mapper.getTeacher(1);
        System.out.println(teacher);

        sqlSession.close();
    }


    @Test
    public void testGetStudents(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();

        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> students = mapper.getStudents();

        for (Student student : students) {
            System.out.println(student);
        }

        sqlSession.close();
    }

    @Test
    public void testGetStudents2(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();

        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
        List<Student> students = mapper.getStudents2();

        for (Student student : students) {
            System.out.println(student);
        }

        sqlSession.close();
    }
}
