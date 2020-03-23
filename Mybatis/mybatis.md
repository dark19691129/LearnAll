# mybatis

## 1，概述

### 1.1，什么是框架呢？

框架是整个或部分系统的可重用设计，表现为一组抽象构件及构件实例间交互的方法；另一种定义认为，框架是可被应用开发者定制的应用骨架，前者是从应用方面而后者是从目的方面给出的定义



通俗点：软件开发中的一套解决方案，不同的框架解决的是不同的问题。

使用框架的好处：框架封装了很多的细节，使开发者可以使用极简的方式实现功能，大大提高开发效率。

框架其实就是某种应用的半成品，就是一组组件，供你选用完成你自己的系统。



### 1.2，框架解决的问题

三层架构：

- 表现层
  - 是用于展示数据的
- 业务层
  - 是处理业务需求
- 持久层
  - 是和数据库交互的
    - 解决方案：JDBC技术：Connection；PrepareStatement；ResultSet
    - Spring的JdbcTemplate：Spring对Jdbc的简单封装
    - Apache的DBUtils：它和Spring的JdbcTemplate很像，也是对Jdbc的简单封装
    - 但以上这些都不是框架，JDBC是规范；Spring的JdbcTemplate和Apache的DBUtils都只是工具类

### 1.3，Mybatis

- MyBatis 是一款优秀的持久层框架，
- 它支持自定义 SQL、存储过程以及高级映射。
- MyBatis 免除了几乎所有的 JDBC  代码以及设置参数和获取结果集的工作。
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java  Objects，普通老式 Java 对象）为数据库中的记录。
- MyBatis 本是[apache](https://baike.baidu.com/item/apache/6265)的一个开源项目[iBatis](https://baike.baidu.com/item/iBatis), 2010年这个项目由apache software foundation 迁移到了google code，并且改名为MyBatis 。
-  2013年11月迁移到Github。



环境：

- JDK 1.8

- Mysql 5.7

- maven 3.6.1

- IDEA

  

需要掌握的知识

- JDBC
- Mysql
- java基础
- Maven
- Junit



SSM框架：配置文件的

最好的学习方式：看官网文档



### 1.4，如何获取Mybatis

maven仓库：

```xml
<!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.2</version>
</dependency>

```

Github：https://github.com/mybatis/mybatis-3/releases

mybatis官方文档：https://mybatis.org/mybatis-3/zh/project-reports.html



### 1.5，持久化

数据持久化：

持久化就是将程序的数据在持久状态（例如数据库，io文件持久化）和瞬时状态转化（内存的数据是断电即失）的过程

为什么需要持久化，因为有一些对象不能让他丢失。





### 1.6，持久层

Dao层，Service层，Controller层

- 完成持久化工作的代码块
- 层界限十分明确





### 1.7，为什么需要Mybatis

- 帮助程序员将数据存入到数据库中
- 方便
- 传统的JDBC代码太复杂了，简化，框架。自动化。
- 不用Mybatis也可以，更容易上手，技术没有高低之分
- 优点
  - 简单易学：
  - 灵活：
  - 解除sql与程序代码的耦合：sql和代码的分离，提高了可维护性。
  - 提供映射标签，支持对象与数据库的orm字段关系映射
  - 提供对象关系映射标签，支持对象关系组建维护
  - 提供xml标签，支持编写动态sql。



最重要的一点：使用的人多！



## 2，第一个Mybatis程序

思路：搭建环境--导入Mybatis--编写代码--测试！

### 2.1，搭建数据库

localhost:3306/mybatis

### 2.2，创建项目

1，创建项目

2，删除src目录

3，导入依赖

```xml
   <dependencies>
        <dependency>
            <!--导入mysql驱动-->
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.46</version>
        </dependency>

        <!--导入Mybatis-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>

        <!--junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>

    </dependencies>
```

### 2.3，创建一个模块

![创建项目](D:\Typora图片\Mybatis\创建项目.PNG)

这样的话，当添加一些新的模块时，就可以直接使用父模块的jar包。

#### （1），编写mybatis的核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--核心配置文件-->
<configuration>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;serverTimezone=UTC&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>

</configuration>
```

在IDEA中连接数据库：

1:

![IDEA连接数据库](D:\Typora图片\Mybatis\IDEA连接数据库.PNG)

2:

![要连接的数据库2](D:\Typora图片\Mybatis\要连接的数据库2.PNG)

注意：在IDEA中修改数据库的时区！！！！！！！！！！！！

方法如下：

![修改时区](D:\Typora图片\Mybatis\修改时区.PNG)



#### （2），编写mybatis工具类

```java
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
```

#### （3），实体类（与数据库相关）

```java
package com.cb414.pojo;

//实体类
public class User {

    private  int id;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

```

#### （4），Dao接口

```java
public interface UserDao {
    List<User> getUserList();

}
```

#### （5），接口实现类

原本的JDBC在实现了Dao接口后，需要建立相应的DaoImpl；但使用了mybatis之后，无需再建立DaoImpl

##### （5.1），原本的DaoImpl：

```java
package com.cb414.dao;

import com.cb414.pojo.User;

import java.util.List;

public class UserDaoImpl implements UserDao{

    @Override
    public List<User> getUserList() {
        return null;
    }
}

```

##### （5.2），mybatis中的：

![mybatis中的DaoImpl](D:\Typora图片\Mybatis\mybatis中的DaoImpl.PNG)

UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Dao/Mapper接口或者-->
<mapper namespace="com.cb414.dao.UserDao">
<!-- 与Dao/Mapper接口建立联系   -->
    <select id="getUserList" resultType="com.cb414.dao.UserDao">
    select * from mybatis.user
  </select>
</mapper>
```



### 2.4，测试与易错点

注意点：

```shell
org.apache.ibatis.binding.BindingException: Type interface com.cb414.dao.UserDao is not known to the MapperRegistry.
```

原因是：

你没有在mybatis的核心配置文件中注册UserDao的mapper.xml文件，在mybaits的核心配置文件中加入：

```xml
    <!--每一个Mapper.xml都需要在mybatis核心配置文件中注册！！！！！！！！！！-->
    <!--换句话说就是mybatis不知道那些mapper.xml在哪里，你需要对其进行注册    -->
    <mappers>
        <mapper resource="com/cb414/dao/UserMapper.xml"/>
    </mappers>
```

然后还会有一个错误：

```shell
java.lang.ExceptionInInitializerError
	at com.cb414.dao.UserDaoTest.test(UserDaoTest.java:14)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
Caused by: org.apache.ibatis.exceptions.PersistenceException: 
### Error building SqlSession.
### The error may exist in com/cb414/dao/UserDao/userMapper.xml
### Cause: org.apache.ibatis.builder.BuilderException: Error parsing SQL Mapper Configuration. Cause: java.io.IOException: Could not find resource com/cb414/dao/UserDao/userMapper.xml
	at org.apache.ibatis.exceptions.ExceptionFactory.wrapException(ExceptionFactory.java:30)
	at org.apache.ibatis.session.SqlSessionFactoryBuilder.build(SqlSessionFactoryBuilder.java:80)
	at org.apache.ibatis.session.SqlSessionFactoryBuilder.build(SqlSessionFactoryBuilder.java:64)
	at com.cb414.utils.MybatisUtils.<clinit>(MybatisUtils.java:23)
	... 23 more
Caused by: org.apache.ibatis.builder.BuilderException: Error parsing SQL Mapper Configuration. Cause: java.io.IOException: Could not find resource com/cb414/dao/UserDao/userMapper.xml
	at org.apache.ibatis.builder.xml.XMLConfigBuilder.parseConfiguration(XMLConfigBuilder.java:121)
	at org.apache.ibatis.builder.xml.XMLConfigBuilder.parse(XMLConfigBuilder.java:98)
	at org.apache.ibatis.session.SqlSessionFactoryBuilder.build(SqlSessionFactoryBuilder.java:78)
	... 25 more
Caused by: java.io.IOException: Could not find resource com/cb414/dao/UserDao/userMapper.xml
	at org.apache.ibatis.io.Resources.getResourceAsStream(Resources.java:114)
	at org.apache.ibatis.io.Resources.getResourceAsStream(Resources.java:100)
	at org.apache.ibatis.builder.xml.XMLConfigBuilder.mapperElement(XMLConfigBuilder.java:372)
	at org.apache.ibatis.builder.xml.XMLConfigBuilder.parseConfiguration(XMLConfigBuilder.java:119)
	... 27 more
```

原因是：maven项目的配置文件默认是放在resources目录下面的，而这个项目中我们的Mapper.xml是放在java目录下面的，所以maven项目是扫描不到的！！！！！

![maven资源过滤问题](D:\Typora图片\Mybatis\maven资源过滤问题.PNG)

若是将mapper.xml放在红圈所指的dao目录下面再运行就可以正常运行，但是项目开发中我们不可能每次都手动移动mapper.xml文件！所以在pom.xml文件中加入

```xml
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>

            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
```

相应的测试类：

```java
public class UserDaoTest {
    @Test
    public void test(){
        //获取sqlsession对象
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        //执行
        //面向接口编程，UserDao和UserMapper只是一个接口和实现类的关系
        UserDao userDao=sqlSession.getMapper(UserDao.class);
        List<User> userList=userDao.getUserList();

        for(User user:userList){
            System.out.println(user);
        }
    }

}

```

执行结果：

```shell
User{id=1, name='张三', pwd='123456'}
User{id=2, name='李四', pwd='456789'}
User{id=3, name='赵五', pwd='111111'}
```

易错点回顾：

1. 配置文件没有注册
2. 绑定接口错误
3. 方法名不对
4. 返回类型不对，要注意Mapper中sql语句的id！
5. Maven导出资源不对



## 3，CRUD

### （3.1），namespace

namespace中的包名要和Dao/Mapper接口的一致！！

### （3.2），Select

选择，查询语句

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Dao/Mapper接口或者-->
<mapper namespace="com.cb414.dao.UserMapper">
<!-- 与Dao/Mapper接口建立联系   -->
    <select id="getUserList" resultType="com.cb414.pojo.User">
        SELECT * FROM mybatis.user;
    </select>
</mapper>
```

- id就是对应的namespace中的方法名
- resultType：sql语句执行的返回值
- parameterType：参数的类型

### （3.3），Insert

增添用户

```java
//插入用户
int addUser(User user);
```

```xml
<!--    对象中的属性，可以直接取出来使用-->
    <insert id="addUser" parameterType="com.cb414.pojo.User">
        INSERT into mybatis.user (id, name, pwd) values (#{id},#{name},#{pwd})
    </insert>
```

### （3.4），Update

更新用户

```java
   //修改用户
    int updateUser(User user);
```

```xml
<!--    更新用户-->
    <update id="updateUser" parameterType="com.cb414.pojo.User">
        update mybatis.user set name=#{name},pwd=#{pwd}  where id=#{id};
    </update>
```

### （3.5），Delete

删除用户

```java
//删除用户
int deleteUser(int id);
```

```xml
<!--    删除用户-->
    <delete id="deleteUser" parameterType="int">
        delete from mybatis.user where id=#{id};
    </delete>
```

### （3.6），总结

==注意：增删改等需要提交事务！！！！！！！！！！！！！！！！！==

总的步骤：

- 编写接口
- 编写对应的mapper语句中sql语句
- 测试

接口类：

```java
public interface UserMapper {
    //查询所有用户
    List<User> getUserList();

    //根据id查找用户
    User getUserById(int id);

    //插入用户
    int addUser(User user);

    //修改用户
    int updateUser(User user);

    //删除用户
    int deleteUser(int id);
}
```

Mapper.xml文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Dao/Mapper接口或者-->
<mapper namespace="com.cb414.dao.UserMapper">
<!-- 与Dao/Mapper接口建立联系   -->
    <select id="getUserList" resultType="com.cb414.pojo.User">
        SELECT * FROM mybatis.user;
    </select>

    <select id="getUserById" parameterType="int" resultType="com.cb414.pojo.User">
        SELECT * FROM mybatis.user WHERE id=#{id};
    </select>

<!--    对象中的属性，可以直接取出来使用-->
    <insert id="addUser" parameterType="com.cb414.pojo.User">
        INSERT into mybatis.user (id, name, pwd) values (#{id},#{name},#{pwd})
    </insert>
    
<!--    更新用户-->
    <update id="updateUser" parameterType="com.cb414.pojo.User">
        update mybatis.user set name=#{name},pwd=#{pwd}  where id=#{id};
    </update>

<!--    删除用户-->
    <delete id="deleteUser" parameterType="int">
        delete from mybatis.user where id=#{id};
    </delete>
</mapper>
```

测试类：

```java
public class UserDaoTest {
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
}
```

### （3.7），错误总结

- 标签不要匹配错
- mybatis的核心配置文件中的resource记得用/，而不是用.
- 程序配置文件必须符合规范
- MullPointException，没有注册到资源
- 输出的xml文件中存在中文乱码问题

### （3.8），Map

（野路子，非正式，但也可以在实际开发中进行使用）

假设我们的实体类或者数据库中的表，字段或者参数过多，我们应当考虑使用Map

UserMapper

```java
    //插入用户
    int addUser2(Map<String,Object> map);
```

Mapper.xml

```xml
<!--    使用Map的野路子-->
    <!--    对象中的属性，可以直接取出来使用-->
    <insert id="addUser2" parameterType="map">
        INSERT into mybatis.user (id, pwd) values (#{userId},#{password})
    </insert>
```

测试类

```java
   @Test
    public void testAddUser2(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map map=new HashMap<String,Object>();
        map.put("userId",5);
        map.put("password","123111");

        mapper.addUser2(map);


        //sqlSession.commit();

        sqlSession.close();

    }
```

Map传递参数，直接在sql中取出key即可

对象传递参数，直接在sql中取对象的属性即可

只有一个基本类型参数的情况下，可以直接在sql中取到（当只有一个基本参数时，在mapper.xml文件的sql语句中的paramterType标签可以不用传参）



### （3.9），模糊查询、

注意：防止sql注入的问题

例如在Mapper.xml文件中执行select中，

- sql语句：SELECT * FROM　mybatis.user WHERE id like ？
- 实际效果：SELECT * FROM　mybatis.user WHERE id like 1
- sql注入的话，可能会出现：SELECT * FROM　mybatis.user WHERE id like 1 or 1=1

所以要防止sql注入

即把通配符在sql语句中写死：SELECT * FROM　mybatis.user WHERE id= “%”#{value}“%”



- java代码执行的时候，传递通配符：%

```java
List<User> userList=mapper.getUserLike("%李%");
```

- 在sql拼接中使用的问题

```xml
        SELECT * FROM mybatis.user WHERE name like "%"#{value}"%"
```





Mapper.xml

```xml
<!--    模糊查询-->
    <select id="getUserLike" resultType="com.cb414.pojo.User">
        SELECT * FROM mybatis.user WHERE name like "%"#{value}"%"
    </select>
```

测试类

```java
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
```



## 4，配置解析

### （1），核心配置文件

- mybatis-config.xml
- Mybatis的配置文件包含了会深深影响Mybatis行为的设置和属性信息

```xml
configuration（配置） 
properties（属性） 
settings（设置） 
typeAliases（类型别名） 
typeHandlers（类型处理器） 
objectFactory（对象工厂） 
plugins（插件） 
environments（环境配置） 
environment（环境变量） 
transactionManager（事务管理器） 
dataSource（数据源） 
databaseIdProvider（数据库厂商标识） 
mappers（映射器） 
```

### （2），环境配置（environments）

mybatis可以配置成适应多种环境

不过要记住：尽管可以配置多个环境，但每个SqlSessionFactory实例只能选择一种环境（同意情况下只能选择一种环境）



药学会配置多套运行环境

Mybatis默认的事务管理器就是JDBC，（还有一种选项是MANAGED，但是已经不使用了）

连接池：POOLED（还有两种是UNPOOLED和JND）

### （3），属性

我们可以通过使用properties来使用配置文件

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties  元素的子元素中设置。例如：【db.properties】

编写一个配置文件

db.properties

```properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?useSSL=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8
username=root
password=123456
```

==注意点：==

```xml
<!--以下xml代码会报错，原因是properties放在了其他标签后面！！！-->

<configuration>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;serverTimezone=UTC&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>

    <!--每一个Mapper.xml都需要在mybatis核心配置文件中注册！！！！！！！！！！-->
    <!--换句话说就是mybatis不知道那些mapper.xml在哪里，你需要对其进行注册    -->
    <mappers>
        <mapper resource="com/cb414/dao/UserMapper.xml"/>
    </mappers>

    <properties></properties>

</configuration>
```

错误信息：

```shell
The content of element type "configuration" must match "(properties?,settings?,typeAliases?,typeHandlers?,objectFactory?,objectWrapperFactory?,reflectorFactory?,plugins?,environments?,databaseIdProvider?,mappers?)".
```

大意是：在xml文件中，所有的标签都可以规定其顺序；

也就是说，properties等标签要按照

```shell
(properties?,settings?,typeAliases?,typeHandlers?,objectFactory?,objectWrapperFactory?,reflectorFactory?,plugins?,environments?,databaseIdProvider?,mappers?)
```

的顺序进行摆放

在核心配置文件中引入:

```xml
    <properties resource="db.properties">
        <property name="username" value="root"/>
        <property name="pwssword" value="111111"/>
    </properties>
```

此时的外部配置文件db.properties:

```xml
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?useSSL=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8
username=root
password=123456
```

- 可以直接引入外部文件
- 可以在其中增加一些属性配置
- 如果两个文件有同一个字段，优先使用外部配置文件！！！！就是说上述中有两个password字段，但会优先引用db.properties中的字段！



### （4），别名

Mapper.xml

```xml
<select id="getUserList" resultType="User">
    SELECT * FROM mybatis.user;
</select>
```

核心配置文件：

```xml
<typeAliases>
    <!--        就是说将com.cb414.pojo.User的别名定义为User，在Mapper.xml文件中对com.cb414.pojo.User的引用可以使用别名来代替-->
    <typeAlias type="com.cb414.pojo.User" alias="User"/>
</typeAliases>
```

也可以指定一个包名，Mybatis会在包名下面搜索需要的java.bean

比如：扫描实体类的包，它的默认别名就是这个类的类名，首字母小写。

（也就是：这个包下面的实体类，都会拥有一个别名，而这个别名默认就是这个类的类名首字母小写）

```xml
<typeAliases>
    <package name="com.cb414.pojo"/>
</typeAliases>
```

所以：

在实体类较少的时候，使用第一种

在实体类较多的时候，使用第二种

但第一种相较于第二种，优势在于可以DIY别名；

可是第二种也可以通过使用别名的注解，来为实体类指定一个别名，即：

```java
@Alias("user")
//实体类
public class User {
```



### （5），设置（settings）

![Mybatis一些需要掌握的设置1](D:\Typora图片\Mybatis\Mybatis一些需要掌握的设置1.PNG)

![Mybatis一些需要掌握的设置2](D:\Typora图片\Mybatis\Mybatis一些需要掌握的设置2.PNG)



### （6），其他配置

- ypeHandlers（类型处理器） 
- objectFactory（对象工厂） 
- plugins（插件）
  - mybatis-generator-core
  - mybatis-plus 
  - 通用mybatis



### （7），映射器（mappers）

MapperRegisty：注册绑定我们的Mapper文件（亦即将mapper.xml文件进行注册，以便能够使用）

url方式不使用了。



方式一【推荐使用】

```xml
<!-- 使用相对于类路径的资源引用 -->
<!--每一个xml都需要在mybatis核心配置文件中注册-->
<mappers>
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
  <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
  <mapper resource="org/mybatis/builder/PostMapper.xml"/>
</mappers>
```

方式二：使用class文件绑定注册

```xml
<!-- 使用映射器接口实现类的完全限定类名 -->
<mappers>
  <mapper class="org.mybatis.builder.AuthorMapper"/>
  <mapper class="org.mybatis.builder.BlogMapper"/>
  <mapper class="org.mybatis.builder.PostMapper"/>
</mappers>
```

使用class方式的注意点：

- 接口和他的Mapper文件必须同名
- 接口和他的Mapper文件必须在同一个包下



方式三

```xml
<!-- 将包内的映射器接口实现全部注册为映射器 -->
<mappers>
  <package name="org.mybatis.builder"/>
</mappers>
```

使用class方式的注意点：

- 接口和他的Mapper文件必须同名
- 接口和他的Mapper文件必须在同一个包下



### （8），生命周期和作用域

理解我们之前讨论过的不同作用域和生命周期类别是至关重要的，因为错误的使用会导致非常严重的并发问题。



![生命周期](D:\Typora图片\Mybatis\生命周期.PNG)



#### （1），SqlSessionFactoryBuilder

- 一旦创建了 SqlSessionFactory，就不再需要它了。
- 所以一般将其设置为局部变量

#### （2），SqlSessionFactory

- 说白了就相当于数据库连接池
- SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。
- 因此 SqlSessionFactory 的最佳作用域是应用作用域
- 最简单的就是使用单例模式或者静态单例模式。

#### （3），SqlSession

- 想象成连接到连接池的一个请求
- SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。
- 用完之后就关闭，防止资源被占用



![生命周期2](D:\Typora图片\Mybatis\生命周期2.PNG)

这里面的每一个Mapper都代表一个具体的业务





## 5，解决属性名和字段名不一致的问题

### （5.1），问题

数据库中的字段名：

![数据库字段名](D:\Typora图片\Mybatis\数据库字段名.PNG)

新建一个module测试属性名和字段名不一致的问题

实体类中的属性名：

```java
//实体类
public class User {

    private  int id;
    private String name;
    private String password;

    ...
}
```

测试结果出现问题

```shell
User{id=1, name='张三', password='null'}
```

Mapper中的sql语句实际上就是：

```xml
SELECT * FROM mybatis.user WHERE id=#{id};
相当于
SELECT id,name,pwd FROM mybatis.user WHERE id=#{id};
在这个过程中是有类型处理器将数据中的字段名一个一个映射到实体类的属性名中的，但他不知道pwd该映射给谁？因为实体类中并没有pwd这个属性，只有password这个属性，所以到了最后，程序能运行，但password接收不到确切的值！！
```

解决办法

- 起别名
  - SELECT id,name,pwd as password FROM mybatis.user WHERE id=#{id};
- resultMap



### （5.2），resultMap

结果集映射

Mapper.xml中加入结果集映射

==注意返回值类型一定要是UserMap==

```xml
<resultMap id="UserMap" type="User">
    <!--        column代表的是数据库中的列名，properties代表的是实体类中的属性名-->
    <result column="id" property="id"/>
    <result column="name" property="name"/>
    <result column="pwd" property="password"/>
</resultMap>

<select id="getUserById" resultMap="UserMap">
    SELECT * FROM mybatis.user WHERE id=#{id};
</select>
```

运行结果：

```shell
User{id=1, name='张三', password='123456'}
```

- `resultMap` 元素是 MyBatis 中最重要最强大的元素
- ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。
- 这就是 `ResultMap` 的优秀之处——你完全可以不用显式地配置它们。
- 如果这个世界总是这么简单就好了。 



## 6，日志

### （6.1），日志工厂

如果一个数据库操作，出现了异常，我们需要排错，日志就是最好的助手！！！

曾经：sout，debug

现在：日志工厂

| logImpl | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。 | SLF4J \| LOG4J \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \|  NO_LOGGING |
| ------- | ----------------------------------------------------- | ------------------------------------------------------------ |
|         |                                                       |                                                              |

- SLF4J
- LOG4J【掌握】
- LOG4J2
- JDK_LOGGING
- COMMONS_LOGGING
- STDOUT_LOGGING【掌握】
- NO_LOGGING

在mybatis具体是用哪个日志实现，在配置中指定

mybatis日志设置是没有默认值的，这就是为什么在没有设置日志之前，没有日志功能的原因

使用：STDOUT_LOGGING

报错：

```shell
Caused by: java.lang.ClassNotFoundException: Cannot find class: STDOUT_LOGGING 
```

错误原因：

```xml
<settings>
    <!--value值后面多了个空格！！！
		mybatis对这些细节很严格
	-->
    <setting name="logImpl" value="STDOUT_LOGGING "/>
</settings>
```

正常运行的结果（部分）：

```shell
...

Opening JDBC Connection#打开JDBC连接池
Created connection 172032696.#创建连接对象
#设置自动提交为false
Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@a4102b8]
==>  Preparing: SELECT * FROM mybatis.user WHERE id=?;#预编译工作 
==> Parameters: 1(Integer)	
<==    Columns: id, name, pwd#查询的列名
<==        Row: 1, 张三, 123456#查询的结果
<==      Total: 1#总共有多少个结果
User{id=1, name='张三', password='123456'}#结果是
Resetting autocommit to true on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@a4102b8]
Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@a4102b8]#关闭连接
Returned connection 172032696 to pool.#返回连接对象
```



配置日志的正确打开方式：

```xml
<settings>
    <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```



### （6.2），Log4j

什么是log4j

- Log4j是[Apache](https://baike.baidu.com/item/Apache/8512995)的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是[控制台](https://baike.baidu.com/item/控制台/2438626)、文件、[GUI](https://baike.baidu.com/item/GUI)组件
- 可以控制每一条日志的输出格式
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程
- 可以通过一个[配置文件](https://baike.baidu.com/item/配置文件/286550)来灵活地进行配置，而不需要修改应用的代码



步骤：

1，先导入log4j的包

```xml
<!--导入log4j-->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

2，log4j.properties

```properties
#将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file

#控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=【%c】-%m%n

#文件输出的相关设置
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./log/kuang.log
log4j.appender.file.MaxFileSize=10mb
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=【%p】【%d{yy-MM-dd}】【%c】%m%n

#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

3，配置log4j为日志的实现

```xml
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>
```

4，log4j的使用，直接测试运行即可

```shell

【org.apache.ibatis.logging.LogFactory】-Logging initialized using 'class org.apache.ibatis.logging.log4j.Log4jImpl' adapter.
【org.apache.ibatis.logging.LogFactory】-Logging initialized using 'class org.apache.ibatis.logging.log4j.Log4jImpl' adapter.
【org.apache.ibatis.io.VFS】-Class not found: org.jboss.vfs.VFS
【org.apache.ibatis.io.JBoss6VFS】-JBoss 6 VFS API is not available in this environment.
【org.apache.ibatis.io.VFS】-Class not found: org.jboss.vfs.VirtualFile
【org.apache.ibatis.io.VFS】-VFS implementation org.apache.ibatis.io.JBoss6VFS is not valid in this environment.
【org.apache.ibatis.io.VFS】-Using VFS adapter org.apache.ibatis.io.DefaultVFS
【org.apache.ibatis.io.DefaultVFS】-Find JAR URL: file:/D:/Intellij-Workplace/TestMybatis/mybatis-04/target/classes/com/cb414/pojo
【org.apache.ibatis.io.DefaultVFS】-Not a JAR: file:/D:/Intellij-Workplace/TestMybatis/mybatis-04/target/classes/com/cb414/pojo
【org.apache.ibatis.io.DefaultVFS】-Reader entry: User.class
【org.apache.ibatis.io.DefaultVFS】-Listing file:/D:/Intellij-Workplace/TestMybatis/mybatis-04/target/classes/com/cb414/pojo
【org.apache.ibatis.io.DefaultVFS】-Find JAR URL: file:/D:/Intellij-Workplace/TestMybatis/mybatis-04/target/classes/com/cb414/pojo/User.class
【org.apache.ibatis.io.DefaultVFS】-Not a JAR: file:/D:/Intellij-Workplace/TestMybatis/mybatis-04/target/classes/com/cb414/pojo/User.class
【org.apache.ibatis.io.DefaultVFS】-Reader entry: ����   4 <
【org.apache.ibatis.io.ResolverUtil】-Checking to see if class com.cb414.pojo.User matches criteria [is assignable to Object]
【org.apache.ibatis.datasource.pooled.PooledDataSource】-PooledDataSource forcefully closed/removed all connections.
【org.apache.ibatis.datasource.pooled.PooledDataSource】-PooledDataSource forcefully closed/removed all connections.
【org.apache.ibatis.datasource.pooled.PooledDataSource】-PooledDataSource forcefully closed/removed all connections.
【org.apache.ibatis.datasource.pooled.PooledDataSource】-PooledDataSource forcefully closed/removed all connections.
【org.apache.ibatis.transaction.jdbc.JdbcTransaction】-Opening JDBC Connection
【org.apache.ibatis.datasource.pooled.PooledDataSource】-Created connection 2085002312.
【org.apache.ibatis.transaction.jdbc.JdbcTransaction】-Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@7c469c48]
【com.cb414.dao.UserMapper.getUserById】-==>  Preparing: SELECT * FROM mybatis.user WHERE id=?; 
【com.cb414.dao.UserMapper.getUserById】-==> Parameters: 1(Integer)
【com.cb414.dao.UserMapper.getUserById】-<==      Total: 1
User{id=1, name='张三', password='123456'}
【org.apache.ibatis.transaction.jdbc.JdbcTransaction】-Resetting autocommit to true on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@7c469c48]
【org.apache.ibatis.transaction.jdbc.JdbcTransaction】-Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@7c469c48]
【org.apache.ibatis.datasource.pooled.PooledDataSource】-Returned connection 2085002312 to pool.
```

简单使用

1，在要使用log4j的类中，导入包：

```java
import org.apache.log4j.Logger;
```

2，日志对象，参数为当前类的对象

```java
static Logger logger=Logger.getLogger(UserDaoTest.class);
```

3，日志级别

```java
logger.info("info:这是一个info信息");
logger.debug("debug:这是一个debug信息");
logger.error("error:这是一个error信息");
```



## 7，分页

思考：为什么要分页

- 减少数据的处理量
- 



### （7.1），limit分页

```sql
//语法：默认从0开始，每页输出2个
SELECT * FROM user limit 0,2
//如果只有一个参数值，则默认从0开始，每页显示参数值个结果
```



使用mybatis实现分页，核心是SQL

1，接口

```java
//分页查询
List<User> getUserByLimit(Map<String,Object> map);
```

2，Mapper.xml

```xml
<!--    分页查询-->
<select id="getUserByLimit" parameterType="map" resultType="user">
    SELECT * FROM user limit #{startIndex},#{pageSize}
</select>
```

3，测试

```java
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
```



### （7.2），RowBounds分页（不推荐在开发中使用）

不再使用sql实现分页

1，接口

```java
//分页查询2
List<User> getUserByRowBounds();
```

2，Mapper.xml

```xml
<!--    分页查询2-->
<select id="getUserByRowBounds" resultMap="UserMap">
    SELECT * FROM mybatis.user
</select>
```

3，测试

```java
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
```

### （7.3），分页插件

Mybatis PageHelper

了解即可





## 8，使用注解开发

### （8.1），面向接口编程





### （8.2），使用注解开发

1，注解在接口上实现

```java
@Select("SELECT * FROM mybatis.user")
List<User> getUsers();
```

2，需要在核心配置文件中绑定接口

```xml
<mappers>
    <mapper class="com.cb414.dao.UserMapper"/>
</mappers>
```

3，测试

```java
@Test
public void test(){
    SqlSession sqlSession= MybatisUtils.getSqlSession();

    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> users = mapper.getUsers();

    for (User user : users) {
        System.out.println(user);
    }

    sqlSession.close();
}
```

本质：反射机制实现

底层：动态代理

### （8.3），CRUD

我们可以在工具类创建的时候设置自动提交的设置

```java
return sqlSessionFactory.openSession(true);
```

编写接口

```java
//根据id查询用户
//若有多个参数时，必须加@Param
@Select("SELECT * FROM mybatis.user WHERE id=#{id}")
User getUserById(@Param("id") int id);
```

测试类（与常规一样）

关于@Param注解

- 基本类型的参数或者String类型，需要加上
- 引用类型不需要加
- 如果只有一个基本类型的话，可以忽略，但是建议加上
- 我们在SQL中引用的就是@Param中设定的属性名



### （8.4），#{} 和${}的区别：

#{}能够防止sql注入，进行预编译

而${}只是进行正常的拼接，无法防止sql注入





## 9，Mybatis详细的执行流程

![mybatis执行流程图](D:\Typora图片\Mybatis\mybatis执行流程图.jpg)



## 10，Lombok

- 是一个java库
- 是一个插件
- 是一个构建库



使用步骤：

1. 在IDEA中安装Lombok插件
2. 在项目中导入Lombok包

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.10</version>
</dependency>
```





相应的注解

```java
@Getter and @Setter
@FieldNameConstants
@ToString
@EqualsAndHashCode
@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
@Data
@Builder
@SuperBuilder
@Singular
@Delegate
@Value
@Accessors
@Wither
@With
@SneakyThrows
```

实例：

```java
//实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private  int id;
    private String name;
    private String password;

}
```

@Data：自动生成get，set，toString，equals等方法

@@AllArgsConstructor和@NoArgsConstructor生成有参构造和无参构造两种方法



优缺点：

不谈优点了，先谈缺点：会导致代码的可读性减少！！！！！！！！！！！！

Lombok用来偷懒的～



## 11，多对一的处理

<img src="D:\Typora图片\Mybatis\多对一.jpg" alt="多对一" style="zoom: 67%;" /> 

- 多个学生对应一个老师
- 多个学生 关联一个老师【多对一】
- 一个老师 集合多个学生【一对多】



### （11.1），测试环境搭建

1. 导入lombok
2. 新建实体类Teacher，Student
3. 建立Mapper接口
4. 建立Mapper.xml文件
5. 在核心配置文件中绑定注册我们的Mapper接口或者文件
6. 测试查询是否能够成功



### （11.2），按照查询嵌套处理

```xml
<!--    思路
        查询所有的学生信息
        根据查询出来的学生的tid，寻找对应的老师！类似于子查询
-->
<select id="getStudents" resultMap="StudentTeacher">
    Select * from mybatis.student
</select>
<!--    然而问题是，Student类中使用一个Teacher类作为属性，我们该怎么对这个Teacher属性赋值？？-->
<!--    本质上是不是还是属性名和数据库的字段名不一致的问题，所以我们自然而然的使用ResultMap-->
<resultMap id="StudentTeacher" type="Student">
    <result property="id" column="id"/>
    <result property="name" column="name"/>
    <!--        此时对于Teacher属性，这种复杂的属性我们该怎么写-->
    <!--        对象用association-->
    <!--        集合用collection-->
    <association property="teacher" column="tid" javaType="Teacher" select="getTeacher"/>
</resultMap>

<select id="getTeacher" resultType="Teacher">
    SELECT * FROM teacher WHERE id=#{tid}
</select>
```



### （11.3），根据结果嵌套处理

```xml
<select id="getStudents2" resultMap="StudentTeacher2">
    SELECT s.id sid,s.name sname,t.name tname
    FROM student s,teacher t
    WHERE s.tid=t.id
</select>

<resultMap id="StudentTeacher2" type="Student">
    <result property="id" column="sid"/>
    <result property="name" column="sname"/>
    <association property="teacher" javaType="Teacher">
        <result property="name" column="tname"/>
    </association>
</resultMap>
```



Mysql中的多对一的查询方式：

1. 子查询
2. 连表查询



## 12，一对多处理

比如：一个老师拥有多个学生！

对于老师而言，就是一对多的关系



### （12.1），环境搭建，和上面的一样

实体类：

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private  int id;
    private  String name;
    private int tid;

}

```

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private int id;
    private  String name;

    //一个老师拥有多个学生
    private List<Student> students;
}
```

### （12.2），按照结果嵌套处理

```java
<!--    按结果嵌套查询-->
    <select id="getTeacher" resultMap="TeacherStudent">
        SELECT s.id sid,s.name sname,t.name tname,t.id tid
        FROM student s,teacher t
        WHERE s.tid=t.id AND t.id=#{tid}
    </select>

    <resultMap id="TeacherStudent" type="teacher">
        <result property="id" column="tid"/>
        <result property="name" column="tname"/>
<!--        关联用association
            集合用collections
            javatype：指定属性的类型
            集合中的泛型信息，我们使用ofType
-->
        <collection property="students" ofType="student">
            <result property="id" column="sid"/>
            <result property="name" column="sname"/>
            <result property="tid" column="tid"/>
        </collection>
    </resultMap>

```



### （12.3），按照查询嵌套处理

```java
<!--    子查询进行查询-->

    <select id="getTeacher" resultMap="TeacherStudent">
        SELECT * FROM mybatis.teacher WHERE id=#{tid}
    </select>

    <resultMap id="TeacherStudent" type="Teacher">
        <collection property="students" javaType="ArrayList" ofType="Student" select="getStudentByTeacherId" column="id"/>
    </resultMap>

    <select id="getStudentByTeacherId" resultType="Student">
        SELECT * FROM mybatis.student WHERE tid=#{tid}
    </select>

```



### （12.4），小结

1. 关联-association【多对一】
2. 集合-collection 【一对多】
3. JavaType & ofType
   1. javaType是用来指定实体类中属性的类型
   2. ofType用来指定映射到list或者集合中的pojo类型，泛型中的约束类型（List<User>,指的是User）



注意点：

- 保证SQL的可读性，保证通俗易懂
- 注意一对多和多对一中，属性和字段的问题
- 如果问题不好排查，善用日志工具，建议使用log4j



慢SQL	查出结果的时间太长了～～

面试高频

- Mysql引擎
- InnoDB底层原理
- 索引
- 索引优化





## 13，动态SQL

==什么是动态SQL：指根据不同的条件生成不同的SQL语句==





### （13.1），搭建环境

```sql
DROP TABLE IF EXISTS `bolg`;
CREATE TABLE `bolg` (
  `id` varchar(50) NOT NULL COMMENT '博客id',
  `title` varchar(100) NOT NULL COMMENT '博客标题',
  `author` varchar(30) NOT NULL COMMENT '博客作者',
  `creat_time` datetime NOT NULL COMMENT '创建时间',
  `views` int(30) NOT NULL COMMENT '浏览量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



#插入数据语句
BEGIN;
INSERT INTO `bolg` VALUES ('1', 'Mybatis如此简单', '尹锐', '2019-12-04 20:32:07', 9999);
INSERT INTO `bolg` VALUES ('2', 'Java如此简单2', '尹锐2', '2019-12-04 20:32:07', 1000);
INSERT INTO `bolg` VALUES ('3', 'Spring如此简单', '尹锐', '2019-12-04 20:32:07', 9999);
INSERT INTO `bolg` VALUES ('4', '微服务如此简单', '尹锐', '2019-12-04 20:32:07', 9999);
COMMIT;
```

创建一个基础工程

1. 导包
2. 编写配置文件
3. 编写实体类
4. 编写实体类对应的Mapper接口和Mapper.xml文件



### （13.2），if语句

```sql
    <select id="queryBlogIf" parameterType="map" resultType="Blog">
        SELECT * FROM mybatis.bolg WHERE 1=1
        <if test="title != null">
            AND title = #{title}
        </if>
        <if test="author != null">
            AND author = #{author}
        </if>
    </select>
```

测试类

```java
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
```

### （13.3），choose（when，otherwise）

就类似于java中case语句

xml文件：

```xml
<select id="queryBlogChoose" parameterType="map" resultType="Blog">
    SELECT * FROM mybatis.bolg
    <where>
        <choose>
            <when test="title != null">
                title = #{title}
            </when>
            <when test="author != null">
                AND author=#{author}
            </when>
            <otherwise>
                AND views=#{views}
            </otherwise>
        </choose>
    </where>
</select>
```

测试类：

```java
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
```

运行结果：

```shell
Blog(id=0b83df10 908f 427b 95d2 f12a59c15e85, title=Mybatis如此简单, author=cb414, creatTime=Mon Mar 23 10:09:37 GMT+08:00 2020, views=9999)
Blog(id=4e50797c 2e9c 4990 9867 ebf631a282b7, title=Java如此简单, author=cb414, creatTime=Mon Mar 23 10:09:37 GMT+08:00 2020, views=1000)
Blog(id=221d9eb5 c66c 48b3 a423 a32d07e27591, title=Spring如此简单, author=cb414, creatTime=Mon Mar 23 10:09:37 GMT+08:00 2020, views=9999)
Blog(id=68e6807a bbf9 4c70 8fb4 24edd974a9af, title=微服务如此简单, author=cb414, creatTime=Mon Mar 23 10:09:37 GMT+08:00 2020, views=9999)
```



### （13.4），trim（where，set）

官方文档：

*where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或  “OR”，*where* 元素也会将它们去除。



xml文件

```xml
    <select id="queryBlogChoose" parameterType="map" resultType="Blog">
        SELECT * FROM mybatis.bolg
        <where>
            <if test="title != null">
                title = #{title}
            </if>
            <if test="author!=null">
                and author=#{author}
            </if>
        </where>
    </select>
```

测试类

```java
    @Test
    public void testQueryBlogChoose(){
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        HashMap map=new HashMap();

        //map.put("title","Java如此简单");
        map.put("author","cb414");
        List<Blog> blogs = mapper.queryBlogChoose(map);

        for (Blog blog : blogs) {
            System.out.println(blog);
        }


        sqlSession.close();
    }
```



xml文件

```xml
<update id="updateBlog" parameterType="map">
    update mybatis.bolg
    <set>
        <if test="title != null">
            title=#{title},
        </if>
        <if test="author != null">
            author=#{author}
        </if>
    </set>
    where id = #{id}
</update>
```

测试类

```java
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
```

结果：

```shell
【com.cb414.dao.BlogMapper.updateBlog】-==>  Preparing: update mybatis.bolg SET title=? where id = ? 
【com.cb414.dao.BlogMapper.updateBlog】-==> Parameters: Java如此简单2(String), 4e50797c2e9c49909867ebf631a282b7(String)
【com.cb414.dao.BlogMapper.updateBlog】-<==    Updates: 1
```

所谓的动态sql，实际上还是sql语句，只是我们可以在sql层面上，执行逻辑代码

### （13.5），foreach

xml文件

```xml
<select id="queryBlogForeach" parameterType="map" resultType="Blog">
    SELECT * FROM mybatis.bolg

    <where>
        <foreach collection="ids" item="id" open="and (" close=")" separator="or">
            id=#{id}
        </foreach>
    </where>

</select>
```

测试

```java
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
```



==动态SQL就是在拼接SQL语句，我们只需要保证SQL的正确性，按照SQL的格式，去排列组合就行了==

建议：

- 先在Mysql中写出完整的SQL语句，在对应的去修改成为我们的动态SQL实现通用即可！！！



### （13.6），SQL片段

有的时候，我们可能会将一些功能的部分抽取出来，方便服用

1. 使用SQL标签抽取公共的部分
2. 在需要使用的地方使用include标签引用即可



注意事项：

- 最好基于单表来定义SQL片段
- 不要存在WHERE标签



xml文件

```xml
    <sql id="if-author-title">
        <choose>
            <when test="title != null">
                title = #{title}
            </when>
            <when test="author != null">
                AND author=#{author}
            </when>
            <otherwise>
                AND views=#{views}
            </otherwise>
        </choose>
    </sql>
    
    <select id="queryBlogChoose" parameterType="map" resultType="Blog">
        SELECT * FROM mybatis.bolg
        <where>
            <include refid="if-author-title"></include>
        </where>
    </select>
```





## 14，缓存

### （14.1），简介

```c
查询：连接数据库很消耗资源
    优化：一次查询的结果，给他暂存在一个可以直接取到的地方--》内存：缓存
    
我们再次查询到相同的数据的时候，直接走缓存，不同走数据库    
```

什么是缓存

- 存在内存中的临时数据
- 将用户进场查询的数据放在缓存中，用户去查询数据就不用从磁盘（关系型数据库数据文件）查询，从缓存中查询，从而提高查询效率，解决了高并发系统的性能问题



为什么使用缓存

- 减少和数据库的交互，减少系统开销，提高系统效率



什么样的数据能使用缓存？

- 进场查询并且不经常改变的数据



MyBatis 内置了一个强大的事务性查询缓存机制，它可以非常方便地配置和定制。 为了使它更加强大而且易于配置，我们对 MyBatis 3  中的缓存实现进行了许多改进。 

默认情况下，只启用了本地的会话缓存（一级缓存），它仅仅对一个会话中的数据进行缓存。 要启用全局的==二级缓存==，只需要在你的 SQL 映射文件中添加一行： 

```xml
<cache/>
```



### （14.2），一级缓存

一级缓存是默认开启的，只在一次SqlSession开启到关闭的区间段

一级缓存相当于一个Map





测试步骤：

1. 开启日志
2. 测试在一个Session中查询两次相同的记录
3. 查看日志输出



注意：

- 映射语句文件中的所有 select 语句的结果将会被缓存。 
- 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。 
  - 因为增删改操作可能会改变原来的数据
  - 而且若在下面的例子的两个查询中，加入一个更新操作，查询得出的User和User2不是同一个对象！！！！！！！
- 缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存。 
- 缓存不会定时进行刷新（也就是说，没有刷新间隔）。 
- 缓存会保存列表或对象（无论查询方法返回哪种）的 1024 个引用。 
- 缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。 

也可以通sqlSession.clearCache();来清除缓存，



测试类：

```java
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
```

运行结果：

```shell
【org.apache.ibatis.transaction.jdbc.JdbcTransaction】-Opening JDBC Connection
【org.apache.ibatis.datasource.pooled.PooledDataSource】-Created connection 2024453272.
【com.cb414.dao.UserMapper.getUserById】-==>  Preparing: SELECT * FROM mybatis.user WHERE id=? 
【com.cb414.dao.UserMapper.getUserById】-==> Parameters: 1(Integer)
【com.cb414.dao.UserMapper.getUserById】-<==      Total: 1
User(id=1, name=张三, pwd=123456)
===============华丽的分割线================
User(id=1, name=张三, pwd=123456)
true
【org.apache.ibatis.transaction.jdbc.JdbcTransaction】-Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@78aab498]
【org.apache.ibatis.datasource.pooled.PooledDataSource】-Returned connection 2024453272 to pool.
```



### （14.3），二级缓存

- 二级缓存也称全局缓存，一级缓存作用域太低了，所以诞生了二级缓存
- 基于nameSpace级别的缓存，一个名称空间，对应一个二级缓存
- 工作机制
  - 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中；
  - 如果当前会话关闭了，这个会话对应的一级缓存就没了；但是我们想要的是，会话关闭了，一级缓存中的数据被保存到二级缓存中
  - 新的会话查询信息，就可以从二级缓存中获取内容
  - 不同的mapper查出的数据会放在自己对应的缓存（map）中



步骤：

1. 开启全局缓存

   1. ```xml
      <!--        显式的开启全局缓存-->
              <setting name="cacheEnabled" value="true"/>
      ```

2. 在要使用二级缓存的Mapper.xml开启

   1. ```xml
      <!--    在当前Mapper.xml中开启二级缓存
              方式是FIFO
              每个60秒刷新一次缓存
              最多只能存512个缓存
              是否只读：是
      -->
          <cache  eviction="FIFO"
                  flushInterval="60000"
                  size="512"
                  readOnly="true"/>
      <!--也可以自定义参数-->
      ```

   2. <!--也可以自定义参数-->

3. 测试

   1. 问题：我们需要将实体类序列化！否则就会报错

      ```java
      public class User implements Serializable {
       
          ....
              
      }
      ```

   2. 



测试二级缓存的工作机制:

Mapper.xml文件

```xml
<!--    在当前Mapper.xml中开启二级缓存
        方式是FIFO
        每个60秒刷新一次缓存
        最多只能存512个缓存
        是否只读：是
-->
    <cache  eviction="FIFO"
            flushInterval="60000"
            size="512"
            readOnly="true"/>

    <select id="getUserById" resultType="User">
        SELECT * FROM mybatis.user WHERE id=#{id}
    </select>

```

测试类：

```java
    /**
     * 测试二级缓存
     * 其实从这里就能看出一个重点：此处的一级缓存在sqlSession关闭的时候已经清除掉了，并转存至二级缓存了，但是此时用户进来之后，并未再进行第二次的语句查询，由此可知，他进来是先查询二级缓存是否有
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
```

运行结果：

```java
【com.cb414.dao.UserMapper】-Cache Hit Ratio [com.cb414.dao.UserMapper]: 0.0
【org.apache.ibatis.transaction.jdbc.JdbcTransaction】-Opening JDBC Connection
【org.apache.ibatis.datasource.pooled.PooledDataSource】-Created connection 2005733474.
【com.cb414.dao.UserMapper.getUserById】-==>  Preparing: SELECT * FROM mybatis.user WHERE id=? 
【com.cb414.dao.UserMapper.getUserById】-==> Parameters: 1(Integer)
【com.cb414.dao.UserMapper.getUserById】-<==      Total: 1
User(id=1, name=张三, pwd=123456)
【org.apache.ibatis.transaction.jdbc.JdbcTransaction】-Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@778d1062]
【org.apache.ibatis.datasource.pooled.PooledDataSource】-Returned connection 2005733474 to pool.
=======================超级华丽的分割线===============================
【com.cb414.dao.UserMapper】-Cache Hit Ratio [com.cb414.dao.UserMapper]: 0.5
User(id=1, name=张三, pwd=123456)
true
```



小结：

- 只要开启了二级缓存，在同一个Mapper下就有效
- 所有的数据都会先放在一级缓存中
- 只有当会话提交，或者关闭的时候，才会提交到二级缓存中～



### （14.4），缓存原理

![二级缓存和一级缓存](D:\Typora图片\Mybatis\二级缓存和一级缓存.PNG)





### （14.5），自定义缓存--ehcache

自己了解即可





## 15，非学习篇章-sql语句存储

```sql
  
/*
 Navicat Premium Data Transfer
 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : mybatis
 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001
 Date: 05/12/2019 17:56:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bolg
-- ----------------------------
DROP TABLE IF EXISTS `bolg`;
CREATE TABLE `bolg` (
  `id` varchar(50) NOT NULL COMMENT '博客id',
  `title` varchar(100) NOT NULL COMMENT '博客标题',
  `author` varchar(30) NOT NULL COMMENT '博客作者',
  `creat_time` datetime NOT NULL COMMENT '创建时间',
  `views` int(30) NOT NULL COMMENT '浏览量'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bolg
-- ----------------------------
BEGIN;
INSERT INTO `bolg` VALUES ('1', 'Mybatis如此简单', '尹锐', '2019-12-04 20:32:07', 9999);
INSERT INTO `bolg` VALUES ('2', 'Java如此简单2', '尹锐2', '2019-12-04 20:32:07', 1000);
INSERT INTO `bolg` VALUES ('3', 'Spring如此简单', '尹锐', '2019-12-04 20:32:07', 9999);
INSERT INTO `bolg` VALUES ('4', '微服务如此简单', '尹锐', '2019-12-04 20:32:07', 9999);
COMMIT;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(10) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `tid` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fktid` (`tid`),
  CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
BEGIN;
INSERT INTO `student` VALUES (1, '小明', 1);
INSERT INTO `student` VALUES (2, '小红', 1);
INSERT INTO `student` VALUES (3, '小张', 1);
INSERT INTO `student` VALUES (4, '小李', 1);
INSERT INTO `student` VALUES (5, '小王', 1);
COMMIT;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` int(10) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
BEGIN;
INSERT INTO `teacher` VALUES (1, '秦老师');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `pwd` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, '狂神', '123456');
INSERT INTO `user` VALUES (2, '张三', '123456');
INSERT INTO `user` VALUES (3, '李四', '123890');
INSERT INTO `user` VALUES (4, '王五', '23333');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
```

