package com.cb414.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils {

    //提升SqlSessionFactory sqlSessionFactory的作用域
    private static SqlSessionFactory sqlSessionFactory;


    static {

        //使用mybatis的第一步：获取sqlsessionfactory对象
        try {
            //指定mybatis核心配置文件
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
     * SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。你可以通过 SqlSession 实例来直接执行已映射的 SQL 语句
     */
    public static SqlSession getSqlSession() {
        //优化一下
//            SqlSession sqlSession=sqlSessionFactory.openSession();
//            return sqlSession;
        return sqlSessionFactory.openSession();
    }


}
