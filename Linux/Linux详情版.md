# Linux

注意：sshd远程登录服务的端口是22！22！22！

注意：centos新版防火墙不再是iptables，而是firewall



## 1，Linux的基本原则

1. 由目的单一的小程序组成：组合小程序完成复杂任务

2. 一切皆文件

3. 尽量避免捕获**用户接口**（Shell）

   用户接口分为：命令行接口（命令行Shell），图形用户接口（图形化Shell）

   命令行用户接口：bash,ksh,tcsj,zsh等

   图形用户接口：Windows,X-Window

4. 配置文件保存为文本格式



 ## 2，命令

命令：应用程序执行的入口

例如一个大型的游戏安装目录下，有多个可执行文件，这些文件就可以理解成应用程序执行的入口，这些入口就是我们理解的命令

任何一个命令基本都能对应一个文件：例如vi命令，对应的是：/bin/vi文件

### 2.1，格式

命令 选项 参数



### 2.2，类型

命令的类型可分为两类：内置命令和外部命令

内置命令：bash里面的命令称为内置命令

外部命令：bash外部的命令



### 2.3，查看是内部还是外部命令

type 命令

例如： type ls

结果：（**大意是：ls不是原生的命令，而是"ls --color=auto"命令的别称**）

ls --color=auto:意思就是结果用颜色进行区分类型

若是想使用原生的ls命令，加上"\\"

例如：\ls

"\\"加上命令是为了执行原生命令

-  alias：别名。
-  keyword：关键字，Shell保留字。
-  function：函数，Shell函数。
-  builtin：内建命令，Shell内建命令。
-  file：文件，磁盘文件，外部命令。
-  unfound：没有找到。

![原生命令](D:\Typora图片\博客图片素材\Linux\Linux命令\1\原生命令.PNG)



### 2.4，在线帮助文档

内部命令帮助文档：help

外部命令帮助文档：man	info





### 2.5，程序有两类返回值

执行结果

执行状态：0表示正确，1-255表示错误

![查看命令的执行结果](D:\Typora图片\博客图片素材\Linux\Linux命令\1\查看命令的执行结果.PNG)

## 3，XShell远程登录

远程登录Linux系统

现在最常用的是XShell【远程登录到Linux的软件】

登录之后，涉及文件的上传和下载，常用的是XFtp5【上传和下载文件的软件】

安装好之后，如果要让XShell远程访问Linux系统连接上并成功使用，需要Linux系统中开启sshd服务，这个服务会监听==22==号端口。sshd这个服务是专门让人来连接访问的

端口开得越多，安全性越弱



### 3.1，新建会话

会话其实就是一个连接

会话名称就是连接的名称

主机号是Linux的ip地址，必须保证Linux能够联网，这样才能够分配ip地址。

XShell远程登录Linux后，就可以使用命令来操作Linux系统



## 4，XFtp远程上传下载文件

### 4.1，介绍

XFtp是一个基于windows平台的功能强大的SFTP，FTP文件传输软件。使用了XFtp以后，windows能安全的在UNIX/Linux和Windows PC之间传输文件



### 4.2，XFtp的安装配置

安装：傻瓜式安装即可

配置：协议使用SFTP，端口是22，主机是Linux系统的ip地址

端口记得要是22，因为可能21是没有开放的、

当连接成功后，如果出现乱码





## 5，vi和vim编辑器

所有的Linux都会内建vi文本编辑器

vim具有程序编辑的能力，可以看作是vi编辑器的增强版，可以主动的以字体颜色辨别语法的正确性，方便程序设计。代码补完，编译及错误跳转能力等方便编程的功能特别丰富，在程序员中被广泛使用



### 5.1，正常模式

在正常模式下，我们可以使用快捷键

在vim打开一个档案就直接进入了一般模式（这是默认的模式）。在这个模式当中，你可以使用上下左右按键来移动光标，你可以使用删除字符或者删除整行来处理档案内容，也可以使用复制粘贴来处理你的文件数据

### 5.2，插入模式/编辑模式

在这个模式下，程序员可以输入内容

按下i I o O a A r R等任何一个字母才可以进入编辑模式，一般按i就行了

### 5.3，命令行模式

在这个模式当中，可以提供你相关指令，完成读取，存盘，替换，离开vim，显示行号等的动作则是在此模式中达成的！







## 6，用户登录和注销

切换成管理员：su -用户名

logout：注销用户

注意：logout注销指令在图形运行级别无效，在运行级别3有效





## 7，用户管理

Linux系统是一个多用户多任务的操作系统，任何一个要使用系统资源的用户，都首先必须向系统管理员申请一个帐号，然后以这个账号的身份进入系统

Linux的用户需要至少要属于一个组



### 7.1，添加用户

useradd [选项] 用户名

若是创建的时候没有指定新建的用户是在哪一个组，就会创建一个跟用户同名的组，并将用户置于那个组里面

- useradd -d /home/dog 用户名
  - 给这个用户指定一个家目录
- passwd 用户名
  - 给这个用户指定密码
- 一般来说，当用useradd 用户名创建用户成功之后，会自动的创建和用户同名的家目录
- 也可以通过useradd -d 指定目录，新的用户名，给新创建的用户指定家目录





### 7.2，删除用户

userdel 用户名

两种情况：

- 删除用户，但保留家目录
  - userdel 用户名
- 删除用户和家目录
  - userdel -r 用户名



### 7,3，查询用户信息

id 用户名 





### 7.4，切换用户

su - 切换用户名

切换之后，可以输入exit，退回到原来的用户身份

从权限高的用户切换到权限低的用户是不需要输入密码的，反之则需要



## 8，用户组

### 8.1，增加组

groupadd 组名

### 8.2，删除组

groupdel 组名

### 8.3，增加用户时直接加上组

useradd -g 用户组 用户名

### 8.4，修改用户的组

usermod -g 用户组 用户名



## 9，用户和组的配置文件

**对于用户而言，有一个用户配置文件：/etc/passwd**

​	记录了用户的信息

​	每一行的含义：用户名:口令:用户标识号:组标示号:注释性描述:主目录:登录shell

​	这里的密码实际上是加密的，一般使用x代替了，密码是存在shadow中

**对于组而言，有一个组的配置文件：/etc/group**

​	组配置文件，记录了Linux包含的组的信息

​	每行的含义：组名:口令:组标识号:组内用户列表（一般用户列表我们是看不到的，因为这个文件做了处理）

**还有一个文件，是用来存储密码信息的--口令配置文件（加密的）：/etc/shadow**

​	口令的配置文件

​	每行的定义：登录名:加密口令:最后一次修改时间:最小时间间隔:最大时间间隔:警告时间:不活动时间:失效时间:标志





## 10，运行级别

Linux有7个运行级别

- 第一个运行级别：0
  - 关机
- 第二个运行级别：1
  - 单用户
    - 找回丢失密码
- 第三个运行级别：2
  - 多用户无网络服务
- 第四个运行级别：3
  - 多用户有网络服务
  - 这个是用的最多的
- 第五个运行级别：4(unused)
  - 保留级别(其实就是还没设置，但是不清楚保留下来是干嘛的)
- 第六个运行级别：5
  - 图形 化界面
- 第七个运行级别：6
  - 重启

系统的运行级别配置文件：/etc/inittab



注意。这里的运行级别没有4

指定运行级别的指令：init [012356]



面试题：怎么找回root密码

思路：进入到单用户模式

然后修改root密码

因为进入单用户模式，root不需要密码就可以登录

开机的时候在引导时，输入回车键，看到一个界面输入e，再看到一个新的界面，选中第二行（kernal）再输入e，在这行的最后输入1，然后回车表示回车完成，再次输入b进行引导，会进入到运行级别1，这时候就会默认是root身份，继而使用passwd指令来修改root密码

（这种方式是不可能远程登录的）





## 11，帮助指令

man----manual，手册的简写

help

终极大招：百度





## 12，文件目录指令

### 12.1，pwd指令

显示当前目录的绝对工作路径



### 12.2，mkdir指令

mkdir [选项] 要创建的目录名

​	mkdir  /home/dog

​	在home目录下面创建dog目录

-p 创建多级目录

​	上述的例子中，如果home目录是不存在的，那么命令就会失效

​	所以：mkdir -p /home/animal/cat

​	注意：这里的animal和cat这两个目录都是不存在的

​	所以希望以此创建多级目录，需要加上-p选项



### 12.3，rmdir指令

 rmdir /home/cat

rmdir删除的是空目录，如果是非空目录

应该使用rmdir -rf  /home/cat



### 12.4，touch指令

创建空文件

可以创建多个文件

touch 文件1 文件2

**touch命令**有两个功能：一是用于把已存在文件的时间标签更新为系统当前的时间（默认方式），它们的数据将原封不动地保留下来；二是用来创建新的空文件。

```shell
-a：或--time=atime或--time=access或--time=use  只更改存取时间；
-c：或--no-create  不建立任何文件；
-d：<时间日期> 使用指定的日期时间，而非现在的时间；
-f：此参数将忽略不予处理，仅负责解决BSD版本touch指令的兼容性问题；
-m：或--time=mtime或--time=modify  只更该变动时间；
-r：<参考文件或目录>  把指定文件或目录的日期时间，统统设成和参考文件或目录的日期时间相同；
-t：<日期时间>  使用指定的日期时间，而非现在的时间；
--help：在线帮助；
--version：显示版本信息。
```



### 12.5，cp指令

cp指令拷贝文件到指定目录

基本语法

cp [选项] source dest

常用选项：-r：递归复制整个文件夹

应用案例：将/home/aaa.txt 拷贝到/home/bbb目录下

> cp /home/aaa.txt /home/bbb

将/home/test目录下的多个文件拷贝到/home/test1目录下

> 如果要拷贝整个目录。需要加上-r，表示递归拷贝
>
> cp -r /home/aaa.txt /home/test1

如果再次执行cp -r /home/aaa.txt /home/test1，由于其中已经有了相同的文件，系统可能会询问你是否覆盖

强制覆盖不提醒的方法：

> \cp -r /home/aaa.txt /home/test1





### 12.6，rm指令

rm指令移除文件或者目录

-r表示递归删除

-f表示删除不提示

​	就是说删除后是否提示你确定删除



### 12.7，mv指令

移动文件与目录或者重命名

基本语法：

> mv oldNameFile newNameFile
>
>  例如：
>
>  mv /home/test.txt /user
>
>  就代表将/home/test.txt移动到user目录下面
>
>   mv /home/test.txt /user/test1.txt
>
>  代表将/home/test.txt移动到/user下，并重命名为test1.txt
>
> 若是此时正在home目录下面，而home目录下面有一个aaa.txt
>
>  那么mv aaa.txt pig.txt
>
>  意思就是将aaa.txt改名为pig.txt
>
>  因为此时home目录下已经有一个aaa.txt了 
>
>  mv aaa.txt /user/pig.txt
>
>  这个就是将当前目录下的aaa.txt移动到user目录下，并重命名为pig.txt



### 12.8，cat指令

cat查看文件内容

以只读的方式打开

只能浏览不能修改

-n 显示行号



但由于一次性显示的行数太多了，通常**cat指令是与more指令**一起使用的

> cat /home/test1.txt | more
>
>  more代表分页显示





### 12.9，more指令

more指令是一个基于vi编辑器的文本过滤器，他以全屏幕的方式按页显示文本文件的内容，more指令中内置了若干快捷键。

操作：

```shell
# 空白键：代表向下翻一页
# Enter：代表向下翻一行
# q：代表立即离开more，不再显示内容
# Ctrl+F：向下滚动一屏幕
# Ctrl+B：返回上一屏
# =：输出当前行的行号
# :f：输出文件名和当前行的行号
```



### 12.10，less指令

less指令是用来分屏查看文件内容，他的功能与more指令类似，但是要比more更加强大，支持各种显示终端，less指令在现实文件内容是，并不是一次将文件加载之后才显示的，而是根据显示需要加载内容，对于显示大型文件具有较高的效率

```shell
# 空白键：向下翻动一页
# [pagedown]：向下翻动一页
# [pageup]：向上翻动一页
# /字串：向下搜寻[字串]的功能；n：向下查找；N：向上查找
# ？字串：向上搜寻[字串]的功能：n：向下查找；N：向上查找
# q：离开less这个程序
```







### 12.11，重定向与追加

#### 12.11.1，文件描述符

0表示标准输入流（stdin）

1表示标准输出流（stdout）

2表示标准错误流（stderr）

#### 12.11.2，输出重定向

\>：覆盖重定向

\>>：追加重定向

2>：错误覆盖重定向

2>>：错误追加重定向

&>：全部重定向

 ![重定向、](D:\Typora图片\Linux\Shell\重定向、.PNG)





### 12.12，echo指令

echo指令输出内容到控制台

基本语法

echo [选项] [输出内容]

```shell
# 输出环境变量，输出当前的环境路径
echo $PATH
# 输出文本
echo "hello"
```



### 12.13，head指令

head用于显示文件的开头部分内容，默认情况下head指令显示文件的前10行内容

```shell
# 基本语法
#查看文件的前10行内容
head 文件
#查看文件的前5行内容，5可以是任意行数
head -n 5 文件
```





### 12.14，tail指令

tail用于输出文件中尾部的内容，默认情况下tail指令显示文件的后10行的内容

```shell
#基本语法
#查看文件的后10行内容
tail 文件
#查看文件的后5行内容，5可以是任意行数
tail -n 5 文件
#实时追踪该文档的所有更新
#这个命令的意思就是使用tail指令查看内容后，如果没有结束命令
#此时再往文件中写入新的内容，tail指令也会把更新后的文件内容即时的显示出来！！！！！！
tail -f 文件
```





### 12.15，ln指令

软链接也叫符号链接，类似于windows里的快捷方式，主要存放了链接其他文件的路径。

```shell
# 基本语法
ln -s [原文件或者目录][软链接名] #（功能描述：给原文件创建一个软链接）
#实例
#给/home目录下创建一个软链接linkToRoot，链接到root目录
ln -s /root linkToRooot
#使用
#可以直接cd 软链接符号进行跳转
cd linkToRoot
#但是使用pwd查看路径，实际上你还是在原有的目录
pwd
#删除这个软链接
rm -rf linkToRoot
```



### 12.16，history指令

```shell
#查看已经执行过的所有指令
history
#显示最近使用过的10条指令
history 10
#执行力是编号为5的指令
!5
```



## 13，时间日期类

### 13.1，date指令cal指令

```shell
#显示当前时间
date 
#显示当前年份
date+%Y
#显示当前月份
date+%m
#显示当前是哪一天
date+%d
#显示年月日时分秒
#中间的连接符可以自定义，但是前面的+不能省略
date "+%Y-%m-%d %H:%M:%S"


#date指令-设置日期
#基本语法
date -s 字符串时间
#-s代表设置时间
#案例1：设置系统时间
date -s "2018-10-10 11:22:22"
#查看当前时间
date



#cal指令
#查看日历的指令
#基本语法
cal[选项] #（不加选项表示显示本月日历）
#查看本月日历
cal
#显示2020年日历
cal 2020
```





## 14，搜索查找类

### 14.1，find指令

find指令将从指定目录向下递归的遍历其各个子目录，将满足条件的文件或者目录显示在终端

```shell
find [搜索范围] [选项]
#搜索范围：往往指的是某个目录下面
#选项说明
-name<查询方式>#按照指定的文件名查找模式查找文件
-user<用户名>#查找属于指定用户名的所有文件
-size<文件大小>#按照指定的文件大小查找文件

# 案例一按文件名：根据名称查找/home目录下面的hello.txt文件在哪里
find /home -name hello.txt
# 案例二按拥有者：查找/opt目录下，用户名称为nobody的文件
find /opt -user nobody
# 案例三查找整个linux'系统下大于20m的文件
# 解释：
# +20M：大于20M  
# -20M：小于20M 
# 20M：等于20M
find / -size +20M
```



### 14.2，locate指令

locate指令可以快速定位文件路径，==locate指令利用事先建立的系统中所有文件名称及路径locate数据库实现快速定位给定的文件，Locate无需遍历整个文件系统，查询速度较快==，为了保证查询结果的准确度，管理员必须定期更新locate时刻

**特别说明：由于locate指令基于数据库进行查询，所以第一次运行的时候，必须使用updatedb指令创建locate数据库**

```shell
# 假如我要找hello.txt
# 第一次使用的话：
updatedb
locate hello.txt
```



### 14.3，grep指令和管道符号|

grep过滤查找，管道符"|"表示将前一个命令的处理结果输出传递给和后面的命令处理，grep常和管道一起使用

```shell
grep [选项] 查找内容 源文件

#选项
-n #显示匹配行以及行号
-i #忽略字母大小写

# 案例：查找hello.txt中含有yes的行，并且显示行号
# 与管道配合使用
# hello.txt里面有一个：yes。一个：Yes
# 加了-i之后忽略大小写，应该指的是过滤的内容忽略大小写
cat hello.txt | grep -n yes
```



## 15，压缩和解压缩类

### 15.1，gzip&gunzip指令

gzip用于压缩文件

gunzip用于解压文件

```shell
# 基本语法
# 使用gzip进行压缩过后，原来的文件就不会保留了
gzip 文件 # 压缩文件，只能将文件压缩为*.gz文件
gunzip文件.gz # 解压缩文件命令

# 实例
# gzip压缩，将/home下的hello.txt文件进行压缩
gzip /home/hello.txt
# gunzip压缩，将/hone下的hello.txt.gz文件进行解压
gunzip /home/hello.txt.gz
```



### 15.2，zip&unzip指令

zip用于压缩文件

unzip用于解压文件，这个在项目打包发布中非常有用

```shell
# 基本语法
# 这里的 将要压缩的内容可以是文件可以是目录
zip [选项] xxx.zip 将要压缩的内容 #（功能描述：压缩文件和目录的命令）
unzip [选项] [解压缩的路径] xxx.zip # 功能描述：解压缩文件
# zip的常用选项
-r #递归压缩，即压缩目录
# unzip的常用选项
-d<目录> # 指定解压文件后文件的存放目录

# 案例一：将/home下的所有文件进行压缩成mypackage.zip
zip -r mypackage.zip /home/
# 案例二：将mypackage.zip解压到/opt/tmp目录下
unzip -d /opt/tmp mypackage.zip  
```

​	

### 15.3，tar指令

tar指令是打包指令，最后打包后的文件.tar.gz的文件

```shell
# 基本语法
tar [选项] xxx.tar.gz 打包的内容 # （功能描述：打包目录，压缩后的文件格式.tar.gz）

# 选项说明
-c #产生.tar打包文件 
-v #显示详细信息
-f #指定压缩后的文件名
-z #打包同时压缩
-x #解包.tar文件

# 案例1：压缩多个文件，将/home/a1.txt和/home/a2.txt压缩成a.tar.gz
# -zcvf是一套组合拳，常规下就这么用
tar -zcvf a.tar.gz a1.txt a2.txt	
# 案例2：将/home得文件夹压缩成myhome.tar.gz
tar -zcvf myhome.tar.gz /home
# 案例3：将a.tar.gz解压到当前目录
tar -zxvf a.tar.gz
# 案例4：将myhome.tar.gz解压到/opt目录下
# 指定解压到某个目录需要加-C选项，且目标目录必须存在
tar -zxvf myhome.tar.gz -C /opt
```



## 16，组管理与权限管理（重难点）

### 16.1，Linux组的基本介绍

文件的权限：

所有者

所在组（其实就是文件的所有者所在的组）

其他用户



### 16.2，查看&修改文件/目录的所有者

一般为文件的创建者，谁创建了该文件，谁就是文件的所有者

查看文件的所有者：

```shell
# 查看文件的所有者
# 指令
ls -ahl
# 应用实例：创建一个组police，再创建一个用户tom，然后使用tom来创建一个文件，看看情况如何
groupadd police
useradd -g police tom
# 指定指令
passwd tom
# 可以登录tom的账号进行文件的创建
touch ok.txt
# 查看文件的详细信息
ls -ahl

# 修改文件的所有者
# 指令
chown 用户名 文件名
# 应用案例：使用root创建一个文件apple.txt，然后将其所有者改为tom
touch apple.txt
chown tom apple.txt # 虽然所有者变化了，但是文件的所在组还是root！！！
```



### 16.3，组的创建

```shell
groupadd 组名

# 案例 创建一个组叫monster
groupadd monster
# 将fox添加到monster组中
useradd -g monster fox
```



### 16.4，查看&修改文件/目录所在组

```shell
# 查看文件/目录所在组
# 基本指令
ls -ahl

# 修改文件所在组
chgrp 组名 文件名
chgrp police ok.txt
```



### 16.5，其他组

除了文件的所有者和所在组的用户外，系统的其他用户都是文件的其他组

```shell
# 改变用户的所在组
usermod -g 组名 用户名
# 也可以改变用户登录是的初始目录
usermod -d 目录名 用户名 改变该用户登录的初始目录

# 案例
# 把tom从police组变成bandit组
usermod -g bandit tom
```





### 16.6，权限的管理

#### 16.6.1，权限详解



执行 ls -l 后每一列的含义

**文件的类型：**

#### 16.6.2，修改权限

chmod

通过chmod修改文件或者目录的权限

**第一种方式：+ - =变更权限**

u：所有者

g：所在组

o：其他用户

a：所有人

```shell
chmod u=rwx,g=rw,o=r 文件目录名
```



**第二种方式：通过数字修改权限**

r=4，w=2，x=1（rwx对应二进制的写法）

所以可以这样写：

```shell
# rwxr-x--x
chmod 751 文件目录名
```



#### 16.6.3，修改文件的所有者（进一步学习）

```shell
chown newowner file  # 改变文件的所有者 但是文件的所在组并未改变
chown newowner:newgroup file # 改变用户的所有者和所在组
-R # 如果是目录，则使其下的所有子文件和目录递归修改


```



#### 16.6.4，改变文件的所在组

```shell
chgrp newgroup file # 改变文件的所在组
# 也可以继续加-R实现递归修改
```



## 17，任务调度

#### 17.1，crond任务调度

crond进行定时任务的设置

![定时任务调度](D:\Typora图片\Linux\实操篇\定时任务调度.PNG)

##### 17.1.1，概述：

任务调度：是指系统在某个时间执行的特定的命令或者程序

任务调度的分类：1，系统工作，有些重要的工作必须周而复始的执行，如病毒扫描等

2，个别用户工作，个别用户可能希望执行某些程序，比如对mysql数据库的备份

基本语法

```shell
crond [选项]
# 常用选项
-e # 编辑crontab问题
-l # 编辑crontab任务
-r # 删除当前用户所有的crontab任务
```



##### 17.1.2，快速入门

案例：

​	设置任务调度文件：/etc/crontab

​	设置个人任务调度。执行crontab -e命令

​	接着输入任务到调度文件

​	如：*/1\*\*\*\*ls -l /etc/>/tmp/to.txt

​	意思说每小时的每分钟执行ls -l /etc/>/tmp/to.txt命令

步骤如下：

```shell
# 如果只是简单的任务，可以不用写脚本，直接在crontab中加入任务即可
# 对于比较复杂的任务，就需要写脚本了（shell编程）
crontab -e
# */1 代表分钟
# 第二个*代表小时
# 第三个*代表第几天
# 第四个*代表第几月
# 第五个*代表星期
*/1 * * * * ls -l /etc>> /tmp/to.txt
# 当保存退出后就会生效了
# 这样就会在每一分钟就会自动调用ls -l /etc>> /tmp/to.txt这个命令

```

##### 17.1.3，参数说明

![任务调度占位符的说明](D:\Typora图片\Linux\实操篇\任务调度占位符的说明.PNG)

![任务调度特殊符号的说明](D:\Typora图片\Linux\实操篇\任务调度特殊符号的说明.PNG)

![任务调度特定时间案例](D:\Typora图片\Linux\实操篇\任务调度特定时间案例.PNG)





## 18，磁盘分区

#### 18.1，分区基础知识

mbr分区：

1. 最多支持四个主分区
2. 系统只能安装在主分区
3. 扩展分区要占一个主分区
4. MBR最大只能支持2TB，但拥有最好的兼容性



gpt分区：

1. 支持无限多个主分区（但操作系统可能限制，比如windows下最多128分区）
2. 最大支持18EB的大容量（EB=1024PB，PB=1024TB）
3. windows764为以后支持gtp

![windows磁盘分区](D:\Typora图片\Linux\实操篇\windows磁盘分区.PNG)





#### 18.2，Linux分区

**原理介绍**

1. Linux来说无论有几个分区，分给哪一个目录使用，他归根结底就只有一个根目录，一个独立且唯一的文件结构，Linux中每个分区都是用来组成整个文件系统的一部分
2. Linux采用了一种叫“载入”的处理方法，他的整个文件系统中包含了一整套的文件和目录，且将一个分区和一个目录联系起来，这是要载入的一个分区将使它的存储空间在一个目录下获得

![Linux分区原理](D:\Typora图片\Linux\实操篇\Linux分区原理.PNG)

硬盘说明

1. Linux硬盘分为IDE硬盘和SCSI硬盘，目前基本是SCSI硬盘
2. 对于IDE硬盘，驱动器标识符为“hdx～”，其中hd表示分区所在设备的类型，这里是指IDE硬盘。x为盘号（a为基本盘，b为从属盘，c为辅助主盘，d为辅助从属盘），～表示分区，前四个分区用数字1到4表示，他们是主分区或者扩展分区，从5开始是逻辑分区。例如：hda3表示第一个IDE硬盘上的第三个主分区或者扩展分区，hdb2表示第二个IDE硬盘上的第二个主分区或者扩展分区
3. 对于SCSI硬盘则表示为sdx～，SCSI硬盘使用sd来表示分区所在设备类型的，其余则和IDE硬盘的表示方法一样

查看的方法：

```shell
lsblk -f # 查看系统的分区和挂载情况

```

![分区挂载详解](D:\Typora图片\Linux\实操篇\分区挂载详解.PNG)



#### 18.3，挂载的经典案例

需求是给我们的Linux系统增加一个新的硬盘，并且挂载到/home/newdisk目录下

步骤：

**虚拟机添加硬盘（假设新添加的硬盘名为sdb）**

![虚拟机增加硬盘步骤1](D:\Typora图片\Linux\实操篇\虚拟机增加硬盘步骤1.PNG)

**分区**

![虚拟机增加硬盘步骤2](D:\Typora图片\Linux\实操篇\虚拟机增加硬盘步骤2.PNG)

```shell
fdisk /dev/sdb
```

**格式化**

![虚拟机增加硬盘步骤3](D:\Typora图片\Linux\实操篇\虚拟机增加硬盘步骤3.PNG)

```shell
# mkfs是格式化的意思
# 把硬盘格式化为ext4类型
mkfs -t ext4 /dev/sdb1
```

**挂载**

![虚拟机增加硬盘步骤4](D:\Typora图片\Linux\实操篇\虚拟机增加硬盘步骤4.PNG)

```shell
# 创建目录
mkdir /homt/newdisk
# 挂载--吧分区和文件目录形成关系
mount /dev/sdb1 /home/newdisk
# 这只是属于临时挂载，当重启后就会失效。所以需要设置永久挂载（下一步）
```

**设置可以自动挂载**

```shell
vim /etc/fstab
# 这个文件记载了分区和挂载点的情况
```

![etc-fstab文件](D:\Typora图片\Linux\实操篇\etc-fstab文件.PNG)

```shell
mount -a # 自动挂载
```



#### 18.4，磁盘情况查询

```shell
# 基本语法
# 查询磁盘的整体使用情况
df -lh

# 查询指定目录的磁盘占用情况
# 查看目录的占用情况
du -h
# 选项
-s # 指定目录占用大小汇总
-h # 带计量单位
-a # 含文件，代表把文件也一起统计进去
--max-depth=1 # 子目录深度
-c # 列出明细的同时，增加汇总值

# 实例：
# 查询/opt目录的磁盘占用情况，深度为1
du -ach  --max-depth=1 /opt

```



#### 18.5，磁盘情况-工作实用指令

```shell
# 统计/home目录下面文件的数目
# "^-"
ls -l /home | grep "^-" | wc -l

# 统计/home目录下目录的个数
ls -l /home | grep "^d" | wc -l

# 统计/home文件夹下文件的个数，包括子文件里的
ls -lR /home | grep "^-" | wc -l

# 统计/home文件夹下目录的个数，包括子文件里的
ls -lR /home | grep "^d" | wc -l

# 以树形显示目录结构
tree
# 但注意，第一次使用需要：
yum install tree

```



#### 18.6，关机&重启指令

shutdown 

- shutdown -h now：表示立即关机
- shutdown -h 1：表示1分钟后关机
- shutdown -r now：表示立即重启

halt

- 就是直接使用，效果等价于关机

reboot

- 就是重启系统

sync

- 把内存的数据同步到磁盘上！！！
- 建议关机之前都先执行一次sync







## 19，网络配置

### 19.1，Linux网络配置原理图（含虚拟机）

目前我们的网络配置采用的是NAT

![网络配置-NAT](D:\Typora图片\Linux\实操篇\网络配置-NAT.png)

查看虚拟网络编辑器

修改ip地址（修改虚拟网卡的ip地址）

查看网关

查看wimdows环境中的VMWareNet8网络配置

```shell
ipconfig
```





### 19.2，Linux网络环境配置

1. 自动获取
   1. 登录后，通过界面来设置自动获取ip
   2. 特点：Linux启动后会自动获取ip，缺点是每次获取的ip地址可能不一样
   3. 不适合做服务器的ip，因为服务的ip需要是固定的
2. 指定固定的ip
   1. 直接修改配置文件来指定ip，并可以连接到外网，
   2. vi编辑/etc/sysconfig/network-scripts/ifcfg-eth0
   3. 修改后一定要重启服务
      1. service network restart
      2. reboot

![网络配置文件](D:\Typora图片\Linux\实操篇\网络配置文件.PNG)





## 20，进程管理

### 20.1，基本介绍

1. 在Linux中，每个执行的程序都称作一个进程，每一个进程都分配一个ID号
2. 每一个进程都会对应一个父进程，而这个父进程可以复制多个子进程，例如www服务器
3. 每个进程都可能以两种方式存在的，前台与后台，所谓前台进程就是用户目前的屏幕上可以进行操作的。后台进程实际上是实际在操作的，但由于屏幕上无法看到的进程，通常使用后台方式执行
4. 一般系统的服务都是以后台进程的方式存在的，而且都会常驻在系统之中。直到关机才会结束



显示系统执行的进程

基本介绍：ps指令是用来查看目前系统中，有哪些正在执行以及他们的执行状况，是可以不加任何参数的

```shell
# ps显示的信息选项
# PID：进程识别号
# TTY：终端机号
# TIME：此进程所消的CPU时间
# CMD：正在执行的命令或者进程名

ps -a # 显示当前终端的所有进程信息
ps -u # 以用户的格式显示进程信息
ps -x # 显示后台进程运行的参数

ps -aux

# 可以查看父进程
ps -ef | more

```

![ps-aux结果](D:\Typora图片\Linux\实操篇\ps-aux结果.PNG)

![ps详解](D:\Typora图片\Linux\实操篇\ps详解.PNG)



![ps-ef详解](D:\Typora图片\Linux\实操篇\ps-ef详解.PNG)



### 20.2，终止进程kill和killall

介绍：

若是某个进程执行一半需要停止时，或是已经消耗了很大的系统资源的时候，此时可以考虑停止该进程。使用kill命令。

基本语法：

```shell
# 功能描述：通过进程号来杀死进程
kill [选项] 进程号 
# 通过进程名来杀死进程。也支持通配符，这在系统因为负载过大而变得很慢的时候很有用
killall 进程名称

# 常用选项
# 有时候喜欢kill后面带-9，因为有些进程是核心进程，会忽略kill命令。
-9 #表示强迫进程立即停止

# 删除某个正在登录的用户
# 这里的sshd是负责登录的守护进程--理解为远程登录服务
ps -aux | grep sshd
# 查询到进程号之后
kill 进程号

# 中止远程登录服务sshd，在适当的时候重新开启
# sshd的命令是由/usr/bin/sshd执行的
# 查询含有sshd的进程，找到进程号
kill 进程号

# 中止多个gedit编辑器
# 这里通过killall 进程名来删除进程
killall gedit

# 强制杀掉一个进程
# 只有CMD一列中是/bin/bash就代表这个进程是一个终端
kill -9 进程号
```



### 20.3，查看进程树

```shell
# 可以更加直观的来查看进程
pstree [选项]

#　常用选项
-p # 显示进程的PID
-u # 显示进程的所属用户
```



### 20.4，服务管理

介绍：

服务（Service）本质就是进程，但是是运行在后台的，通常会监听某个端口，等待其他程序的请求，比如（mysql，sshd 防火墙等），因此我们又称为守护进程，是Linux中非常重要的知识点





service管理指令

```shell
service 服务名 start | stop | restart | reload | status
# 在Centos7.0以后不再使用service，而是使用systemctl

# 例如关闭防火墙
systemctl iptables stop

# 可以通过telnet指令检查linux的某个端口是否在监听，并且可以访问
telnet ip 端口

# 但这种设置只会在这次启动时生效，当重启过后就会恢复原状，
# 要想永久设置，需要使用chkconfig

# 查看服务名
# 1，使用setup，系统服务可以看到
# 2，/etc/init.d/服务名称
ls -l /etc/init.d

```

服务管理的运行级别

查看或者修改默认级别：

**服务的7种运行级别**

==服务也有运行级别，意思就是服务在哪些运行级别下才会运行！！==

1. 运行级别0：系统停机状态，系统默认运行级别是0，否则不能正常运行
2. 运行级别1：单用户工作状态，root权限，用于系统维护，禁止远程登陆
3. 运行级别2：多用户状态（没有NFS），不支持网络
4. 运行级别3：完全的多用户状态（有NFS），登陆后进入控制台命令行模式
5. 运行级别4：系统未使用，保留
6. 运行级别5：X11控制台，登陆后进入图形GUI模式
7. 运行级别6：系统正常关闭并重启，默认运行级别是不能设置成6的，否则不能正常启动





### 20.5，chkconfig指令

```shell
# 介绍：
# 通过chkconfig命令可以给每个服务的各个运行级别设置自启动/关闭

# 基本语法
# 查看服务 
chkconfig --list | grep xxx
chkconfig 服务名 --list
chkconfig --level 5 服务名 on/off

# 注意，是哟个chkconfig重新设置服务自启动或者关闭之后，需要reboot重启才能生效

# 案例1，请显示当前系统的所有服务的各个运行级别
chkconfig --list

# 案例2，请查看sshd服务的运行状态
systemctl sshd status

# 案例3，将ssjd服务在运行级别5下设置为不自动启动，看看有什么效果
chkconfig --level 5 sshd off

# 案例4，当运行级别为5的时候，关闭防火墙
chkconfig --level 5 iptables off

# 案例5，在所有的运行级别下，关闭防火墙
chkconfig iptables off

# 案例6，在所有的运行级别下，开启防火墙
chkconfig iptables on
```



### 20.6，动态监控进程

```shell
# 介绍
# top与ps命令很相似，他们都用来显示正在运行的进程，top与ps最大的不同之处，在于top在执行一段时间可以更新正在运行的进程

# 基本语法
top [选项]

# 选项说明
# 指定top命令每隔几秒更新，默认是3秒在top命令的交互模式当中可以执行的命令
-d 秒数
# 使top不显示任何闲置或者僵死进程
-i
# 通过指定监控进程id来仅仅监控某个进程的状态
-p


# 交互操作的说明
P # 以CPU使用率进行排序，默认就是此选项
M # 以内存的使用率排序
N # 以PID排序
q # 推出top
k # 输入pid号可以杀死进程


```

![top进程动态监控](D:\Typora图片\Linux\实操篇\top进程动态监控.PNG)







### 20.7，监控网络状态

上述的是进程，这里监听的是网络服务

==这个指令使用频率很高==

查看系统网络情况netstat

```shell
# 基本语法
netstat [选项]
# 一般用
netstat -anp

# 选项说明
-an # 按一定顺序排列输出
-p # 显示哪个进程在调用

# 应用案例 查看服务名为sshd的服务的信息
netstat -anp | grep sshd

```





## 21，RPM和YUM

### 21.1，rpm包的管理

> 介绍：一种用于互联网下载报的打包及安装工具，它包含在某些Linux分发版中。他生成具有.RPM扩展名的文件。RPM是RedHat Package Manager（RedHat软件包管理工具）的缩写，类似于Windows的setup.exe，这一文件格式名称虽然打上了RedHat的标志，但理念是通用的
>
>  Linux的分发版本都有采用（suse，redhat，centos等等），可以算公认的行业标准

rpm包的简单查询指令

```shell
# 查询已安装的rpm列表
rpm -qa | grep xx
```

![rpm](D:\Typora图片\Linux\实操篇\rpm.PNG)

如果是i686，i386表示32为操作系统

noarch表示通用

![rpm的其他查询命令](D:\Typora图片\Linux\实操篇\rpm的其他查询命令.PNG)





卸载rpm包

```shell
# 基本语法
rpm -e RPM包的名称

# 例如：删除firefox的软件包
rpm -e firefox

# 细节：如果其他软件包依赖于你即将删除的那个软件包的话，卸载时就会产生错误信息
# 放弃或者一定要删
# 如果一定要删除，可以寄一个--nodeps参数，但是这种方式并不推荐，因为容易导致那些依赖这个软件包的软件无法正常运行
rpm -e --nodeps 软件包

```



安装rpm包

```shell
# 基本语法
rpm -ivh RPM包全路径名称
# 参数名称
i=install 安装
v=verbose 提示
h=hash 进度条

```







### 21.2，yum

> 介绍：
>
> yum是一个Shell前端软件包管理器，基于RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖性关系（就是说如果你需要安装a软件，而a软件又依赖b软件，它会帮你把b软件一起装了），并且一次安装所有依赖的软件包

![yum的原理](D:\Typora图片\Linux\实操篇\yum的原理.PNG)

yum的基本指令

```shell
# 查询yum服务器是否有需要安装的软件
yum list | grep xx软件列表

# 安装指定的yum包
yum install xxx 下载安装

# 步骤1，先查看firefox rpm在yum服务器上有!
yum list | grep firefox

# 会安装最新版本的firefox
yum install firefox
```



## 22，JavaEE定制篇

![JavaEE定制篇1](D:\Typora图片\Linux\实操篇\JavaEE定制篇1.PNG)

搭建JavaEE开发环境

jdk

tomcat

eclipse

mysql



### 22.1，安装JDK

一般来讲，安装软件都是安装到/opt目录下

步骤：

1. 将软件通过xftp5上传到/opt下
2. 解压缩到/opt
3. 配置环境变量的配置文件vim /etc/profile
   1. JAVA_HOME=/opt/jdk文件
   2. PATH=/opt/jdk文件名/bin:$PATH
   3. export JAVA_HOME PATH
4. 配完之后，需要注销用户，环境变量才能生效
   1. 如果是在运行级别3，logout
   2. 或者不注销，输入source /etc/profile
5. 输入java，javac测试配置成功？

![JavaEE定制篇2](D:\Typora图片\Linux\实操篇\JavaEE定制篇2.PNG)





### 22.2，安装tomcat

步骤：

1. 解压缩到/opt
   1. tar -zxvf tomcat安装包
   2. 切入到tomcat的bin目录，并执行启动脚本
2. 启动tomcat ./startup.sh
3. 开放端口 vim /etc/sysconfig/iptables
   1. 实际上就是修改防火墙文件
      1. vim /etc/sysconfig/iptables 
   2. 重启防火墙
      1. service iptables restart
4. 测试是否安装成功，访问：http ://linuxip:8080





### 22.3，Eclipse的安装

步骤

1. 解压缩到/opt
2. 启动eclipse，配置jre和server
3. 编写jsp页面并测试成功





### 22.4，安装Mysql

略，自行百度







## 23，Shell脚本

Linux运维工程师在进行服务器集群管理的时候，需要编写shell程序来进行服务器管理

对于JavaEE和Python程序员来说，工作的需要，你的老大会要求你编写一些shell脚本进行程序或者是服务器的维护，比如编写一个定期备份数据库的脚本

对于大数据程序员来说，需要编写shell程序来管理集群



### 23.1，Shell是什么

![shell原理](D:\Typora图片\Linux\实操篇\shell原理.png)







### 23.2，Shell编程快速入门



#### 23.2.1，Shell脚本的执行方式



脚本格式要求

1. 脚本要以#!/bin/bash开头
   1. 这句话的其实就是指定这个脚本由谁进行解析
2. 脚本需要有可执行权限



脚本的常用执行方式

1. 方式1：输入脚本的绝对路径或者相对路径
   1. 首先要赋予脚本的可执行权限
   2. 执行脚本
2. 方式2：sh+脚本





#### 23.2.2，Shell的变量

Shell变量的介绍

```shell
# linux shell中的变量分为系统变量，和用户自定义变量
# 系统变量;$HOME,$PWD,#SHELL$USER等等
# 例如：echo $HOME
# 显示当前shell中所有变量

# SHELL变量的定义
# 基本语法
# 注意这里的变量左右两边不能有空格，shell脚本编写中要非常注意空格的问题
# 定义变量：变量=值
# 撤销变量：unset 变量
# 静态静态变量：readonly 变量，注意：不能使用unset来撤销静态变量，否则会报错

# 定义变量的规则
# 变量名称可以有字母，数字和下划线组成，但是不能以数字开头
# 等号两侧不能有空格
# 变量名称一般习惯为大写
# 将命令的返回值赋给变量
# A=`ls -al` 反引号，运行里面的命令并将结果返回给变量A
# A=$(ls -al) 等价于反引号
```



#### 23.2.3，Shell的环境变量

![Shell环境变量](D:\Typora图片\Linux\实操篇\Shell环境变量.PNG)

```shell
# 基本语法
# 功能描述：将设shell变量输出为环境变量
# 在/etc/profile文件下声明一个环境变量
# 然后在其他的文件中就可以引用这个环境变量了
export 变量名=变量值
# 功能描述：让修改后的配置信息立即生效
source 配置文件
# 功能描述：查询环境变量的值
echo $变量名

# 多行注释
:<<!
注释的内容
!

```

变量的引用：

```shell
#!/bin/bash
#假设有一个变量A
#当需要对A进行赋值时
A=3
#当需要对变量A的内容进行操作时，需要引用A变量:$A || ${A}
if [ $A -eq 3 ];then
	echo "A的值为3"
else
	echo "A的值不为3"
fi
```

单引号：强引用，不会做变量替换

双引号：弱引用，会做变量替换

反引号：``命令替换

```shell
# 引用变量
# 例如环境变量A里面存了"Hello"
echo "$A"
# 输出：Hello
# 若要求输出Hellos??
echo "$As"
# 结果为空，因为Bash会把As理解为一个新的变量
# 若要实现，则可以这样做：
echo "${A}s"
# 结果：Hellos
```

![引用变量](D:\Typora图片\Linux\Shell\引用变量.PNG)



**环境变量在父子bash之间的传递**

![环境变量在父子bash之间传递](D:\Typora图片\Linux\Shell\环境变量在父子bash之间传递.PNG)







#### 23.2.4，Shell位置参数变量

```shell
# 当我们执行一个shell脚本的时候，如果希望获得到命令行的参数，就可以使用位置参数变量
# 比如：./mysql.sh 100 200 就是一个执行的shell的命令行，可以在mysql脚本中获取到参数信息

# 基本语法
# n为数字，$0表示命令本身，$1-$9表示第一个到第九个参数，十以上的参数需要用大括号包含，如${10}
$n
# 这个变量代表命令行中的所有参数，$*把所有参数看成一个整体
$*
# 这个变量也代表命令行中的所有参数，不过$@把每个参数区分对待
$@
# 这个变量代表命令行中所有参数的个数
$#
```



#### 23.2.5，Shell预定义变量

```shell
# 介绍
# 就是shell设计者事先已经定义好的变量，可以直接在shell脚本中使用

# 基本语法
# 功能描述：当前进程的进程号
$$ 
# 功能描述：后台运行的最后一个进程的进程号（PID）
$!
# 功能描述：最后一次执行的命令的返回状态。如果这个变量的值为0，证明上一个命令正确执行；如果这个变量的值为非0.（具体是哪个数，由命令自己决定），则证明上一个命令执行不正确
$?

# 实例
echo "当前的进程号=$$"
# 后台的方式运行： &
./脚本名 &
echo "最后的进程号=$!"
echo "执行的值=$?"
```



#### 23.2.6，Shell运算符

```shell
# 基本介绍
# 学习如何在shell中进行各种运算操作

# 基本语法
# 1
$((运算式))
# 案例 注意这里括号的层数
RESULT=$(((2+3)*5))

$[运算式]
# 案例
RESULT2=$[(2+3)*4]


# 2
expr m+n
expr m-n

# 3
# expr \*,/,%
# 这里使用\* 是因为需要进行转义
TEMP=`expr 2+3`
echo "$TEMP"
```



#### 23.2.7，Shell条件判断

```shell
# 基本语法
# 注意condition左右两边要有空格
# 非空返回true，可以使用$?验证（0为true,>1为false）
[ condition ] 

# 常用判断条件
# 两个整数的比较
= 字符串比较
-lt 小于
-le 小于等于
-eq 等于
-gt 大于
-ge 大于等于
-ne 不等于

# 按照文件权限进行判断
-r 有读的权限
-w 有写的权限
-x 有执行的权限

# 按照文件类型进行判断
-f 文件存在并且是一个常规的文件
-e 文件存在
-d 文件存在并是一个目录

# 应用实例
# "ok"是否等于"ok"
if [ "ok" = "ok" ]
then
	echo "equal"
fi

# 23是否大于22
if [ 23 -gt 22 ]
then
	echo "大于"
fi

# 判断/root/useraaa.txt是否存在
if [ -e /root/user/aaa.txt ]
then
	echo "存在"
fi	
```



#### 23.2.8，Shell流程控制if

```shell
# if 判断
# 基本语法
if [ 条件判断式 ];
then
	程序
fi

# 或者
if [ 条件判断式 ]
then
	程序
elif [ 条件判断式 ]	
then
	程序
fi		

```



#### 23.2.9，命令的逻辑关系

0为真，非0则为假（与c语言相反）

逻辑与：&&

逻辑或：||

逻辑非：!

```shell
条件一 && 条件二 || 条件三

#可能会出现的情况：
#1：条件一若为真，则若条件二为真，那么条件三便不执行了；
#若条件二为假，那么（条件一和条件二）组成的判定表达式和条件三是逻辑或的关系，且（条件一和条件二）结果为假，所以条件三会执行

#2：条件一若为假，则（条件一和条件二）组成的判定表达式和条件三是逻辑或的关系，且（条件一和条件二）结果为假，所以条件三会执行
```





#### 23.2.10，Shell流程控制case

```shell
case 语句

# 基本语法
case $变量名 in
"值1")
如果变量的值等于值1，就执行程序1
;;
"值2")
如果变量的值等于值2，则执行程序2
;;
...省略其他分支...
*)
如果变量的值不是以上的值，则执行此程序
;;
esac

# 应用案例，当命令行参数是1的时候，输出"周一"
case $1 in
"1")
echo "周一"
;;
"2")
echo "周二"
;;
"3")
echo "周三"
;;
"4")
echo "周四"
;;
"5")
echo "周五"
;;
"6")
echo "周六"
;;
# 如果都匹配不到，就会到这里来，我们就让这种情况下为周日就好了
"*")
echo "周日"
;;
esac
```







#### 23.2.11，Shell流程控制for

```shell
# 基本语法1
for 变量 in 值1 值2 值3
do 
	程序
done
# 案例
# 这里的$*是当作一个整体来处理，所以只会循环一次
# 如果将其换成$@的话，它里面有多少个参数就会循环多少次
for i in "$*"
do
	echo "the num is $1"
done	

# 基本语法2
# 与java的差不多，但是里面多了小括号
for((初始值;循环控制条件;变量变化))
do
	程序
done	
# 案例
SUM=0
for((i=1;i<=100;i++))
do
	SUM=$[$SUM+$i]
done
echo "SUM is $SUM"

```


```shell
for 变量 in 列表; do

	语句

done
```

如何生成列表

{1..100} 注意：花括号里面不能使用变量

seq [起始数] [跨度数] 结束数

起始数如果不指定的话，默认从1开始



#### 23.2.12，Shell流程控制while

```shell
# 基本语法
while [ 条件判断式 ]
do
	程序
done	
```



#### 23.2.13，read读取控制台的输入

```shell
# 基本语法
read (选项)(参数)
# 选项
-p # 指定读取值时的提示符
-t # 指定读取值时等待的时间（秒），如果没有在指定时间内输入，那么就不等待了

# 参数
# 变量：指定读取值的变量名

# 案例：读取控制台输入一个num值，在10秒内输入
# 这里的NUM1的意思就是说，控制台输入的值由NUM1进行接收
# -t是用来指定10秒内进行输入的
read -t 10 -p "请输入一个NUM值=" NUM1
echo "你输入的值NUM=$NUM1"
```

#### 23.2.14，Shell系统函数

##### 23.2.14.1，系统函数basename

```shell
# 函数介绍
# shell编程和其他编程语言一样，有系统函数，也有自定义函数

# 系统函数（此处只介绍两个）
# basename
# 功能：返回完整路径最后/的部分，常用于获取文件名
# 基本语法
basename [pathname] [suffix]
basename [string] [suffix] # 功能描述：basename命令会删掉所有的前缀包括最后一个/字符，然后将字符串显示出来
# 选项：
# suffix为后缀，如果suffix被指定了，basename会将pathname或者string中的suffix去掉

# 案例1，提取/home/user/aaa.txt的文件名
# 这里的.txt是后缀，也就是说，他会返回一个aaa
# 如果不指定后缀，就会返回aaa.txt
basename /home/user/aaa.txt .txt


```



##### 23.2.14.2，系统函数dirname

```shell
# 基本语法
# 功能：返回完整路径最后/的前面的部分，常用于返回路径部分
dirname 文件绝对路径 # 功能描述：从给定的包含绝对路径的文件名中去除文件名（非目录部分）然后返回剩下的路径（目录部分）
# 应用实例
# 返回/home/user/aaa.txt的/home/user
dirname /home/user/aaa.txt
```



#### 23.2.15，Shell自定义函数

```shell
# 基本语法
# 这里的小括号是不用写形参的
[ function ] funname[()]
{
	Action;# 实际上就是方法体
	[return int;]

}

# 调用直接写函数名：
funname


# 案例
function getSum(){
	SUM=$[$n1+$n2]
	echo "和是=$SUM"
}
read -p "请输入第一个参数n1" n1
read -p "请输入第二个参数n2" n2

# 调用getSum
getSum $n1 $n2
```



#### 23.2.16，注意点



若是输入命令后，显示命令不存在

结果：

**因为每个命令其实就是个可执行文件**

**所以要么是可执行文件不存在**

**要么是这个可执行文件不在环境变量path中**

例如，当输入man命令，显示不存在时

针对可执行文件不存在这种情况，可以使用yum进行程序的安装：yum install man



#### 23.2.17，数据黑洞

/dev/null：（/dev/null）**空设备是一个设备文件，它丢弃所有写到它的数据，但是报告写操作成功。**

空设备通常用于处理进程的不需要的输出流，或作为输入流的方便的空文件。这通常是通过重定向来实现的。

```shell
# 例如这是一个增加用户的脚本
#!/bin/bash 

U=user1
useradd $U
echo $U | passwd --stdin $U &>/dev/null
echo "user add success"

```





### 23.3，Shell编程综合案例

![shell综合案例](D:\Typora图片\Linux\实操篇\shell综合案例.PNG)

```shell
# 需求分析
# 每天凌晨2：10备份数据库atguiguDB到/data/backup/db
# 备份开始和备份结束能够给出相应的提示信息
# 备份后的文件要求以备份时间为文件名，并打包成.tar.gz的形式，比如：2018-03-12_230201.tar.gz
# 在备份的同时，检查是否有10天前备份的文件，如果有就将其删除

# 编写Shell脚本

#!/bin/bash

# 完成数据库的定时备份
# 备份的路径
BACKUP=/data/backup/db
# 当前的时间作为文件名
DATETIME=$(date +%Y_%m_%d_%H%M%S)
# 可以输出变量调试一下
echo $DATETIME # 也可以写成echo ${DATETIME}

# 开始备份
echo "=========开始备份========="
echo "=========备份的路径是$BACKUP/$DATETIME.tar.gz"

# 连接数据库需要一些变量，例如主机名，用户名...
# 主机
HOST=localhost
# 用户名
DB_USER=root
# 密码
DB_PWD=root
# 备份数据库名
DATABASE=atguiguDB
# 创建备份的路径
# 如果备份的路径文件夹存在就使用，否则就创建
[ ! -d "$BACKUP/$DATETIME" ] && mkdir -p "$BACKUP/$DATETIME"

# 执行mysql的备份数据库的指令
mysqldump -u${DB_USER} -p${DB_PWD} --host=$HOST $DATABASE | gzip > $BACKUP/$DATETIME/$DATETIME.sql.gz

# 剩下的就是打包备份文件
cd $BACKUP
tar -zcvf $DATETIME.tar.gz $DATETIME
# 删除临时目录
rm -rf $BACKUP/$DATETIME

# 删除10天前的备份文件
# -mtime +10 这个选项是用来找10天前创建的文件的
# {} \; 是固定写法
find $BACKUP -mtime +10 -name "*.tar.gz" -exec rm -rf {} \;
echo "==========备份成功============"


# 最后就是将脚本交给crond进行调度
crontab -e
10 2 * * * /user/sbin/mysql_db_backup.sh
```





