import com.cb414.dao.BlogMapper;
import com.cb414.pojo.Blog;
import com.cb414.utils.IDUtils;
import com.cb414.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyTest {

    @Test
    public void addInitBlog(){
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        Blog blog=new Blog();
        blog.setId(IDUtils.getId());
        blog.setTitle("Mybatis如此简单");
        blog.setAuthor("cb414");
        blog.setCreatTime(new Date());
        blog.setViews(9999);

        mapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("Java如此简单");
        mapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("Spring如此简单");
        mapper.addBlog(blog);

        blog.setId(IDUtils.getId());
        blog.setTitle("微服务如此简单");
        mapper.addBlog(blog);

        blog.setId(IDUtils.getId());


        sqlSession.close();
    }


    @Test
    public void testQueryBlogIf(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        HashMap map=new HashMap();

        //map.put("title","Java如此简单");
        map.put("author","cb414");
        List<Blog> blogs = mapper.queryBlogIf(map);

        for (Blog blog : blogs) {
            System.out.println(blog);
        }


        sqlSession.close();
    }

    @Test
    public void testQueryBlogChoose(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        HashMap map=new HashMap();

        //map.put("title","Java如此简单");
        map.put("views",1000);
        map.put("author","cb414");
        List<Blog> blogs = mapper.queryBlogChoose(map);

        for (Blog blog : blogs) {
            System.out.println(blog);
        }


        sqlSession.close();
    }

    @Test
    public void testUpdateBlog(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();

        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        HashMap hashMap=new HashMap();
        hashMap.put("title","Java如此简单2");
        hashMap.put("id","4e50797c2e9c49909867ebf631a282b7");

        mapper.updateBlog(hashMap);

        sqlSession.close();
    }

    @Test
    public void testQueryBlogForeacg(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        HashMap map=new HashMap();

        ArrayList ids=new ArrayList();
        ids.add(1);
        ids.add(3);
        ids.add(4);

        map.put("ids",ids);

        List<Blog> blogs = mapper.queryBlogForeach(map);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }

        sqlSession.close();
    }
}

