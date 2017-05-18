# 院系专业信息展示网站

一个用于信息展示的网站, 用户可以查看文章与分类列表. 管理员可编辑文章, 管理分类, 增删管理员等.

# 部署
## 安装必要的工具与依赖
Debian/Ubuntu
```
apt install openjdk-8-jdk maven git mysql-server
```
Redhat/Centos
```
yum install java maven git mysql-server
```

## 运行 MySQL
```
service mysql start
```
(进行用户设置)

## 获取源码
```
git clone https://github.com/czp3009/speciality-website
```

## 配置
```
cd speciality-website
mvn spring-boot:run
```
首次运行后, 将在工作目录生成 ./config/ 文件夹. 

默认的 profile 为 prod, 修改 application-prod.properties 中的数据库连接设置并保存文件.

# 运行与初始化
在项目目录执行
```
mvn spring-boot:run
```
第一次运行将执行一次用户创建向导, 根据命令行提示进行输入后完成初始化.

完毕后, 项目即进入运行状态.
