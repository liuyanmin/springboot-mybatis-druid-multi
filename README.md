# springboot-mybatis-druid-multi
springboot+mybatis+druid 配置多数据源，支持多事务管理，只需在@Transactional(value="dbTransactionManager")指定value即可。
注意: 本项目不支持多库之间的分布式事务。

# 配置说明
## 配置pom依赖
pom.xml 文件添加如下配置:
~~~xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>5.1.47</version>
</dependency>

<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>druid-spring-boot-starter</artifactId>
  <version>1.1.18</version>
</dependency>

<dependency>
  <groupId>org.mybatis.spring.boot</groupId>
  <artifactId>mybatis-spring-boot-starter</artifactId>
  <version>1.3.2</version>
</dependency>

<dependency>
  <groupId>org.mybatis</groupId>
  <artifactId>mybatis</artifactId>
  <version>3.5.0</version>
</dependency>
~~~

## 配置 application.properties
application.properties 是公共配置，可以指定 spring.profiles.active=dev 运行环境，dev-开发 test-测试 prod-线上，数据库配置在各个环境内配置，内容如下：
~~~properties
# DB1数据库配置
spring.datasource.db1.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.db1.url=jdbc:mysql://127.0.0.1:3306/user1?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull
spring.datasource.db1.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.db1.username=root
spring.datasource.db1.password=root

# DB2数据库配置
spring.datasource.db2.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.db2.url=jdbc:mysql://127.0.0.1:3306/user2?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull
spring.datasource.db2.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.db2.username=root
spring.datasource.db2.password=root
~~~

## 配置启动类
启动类开启事务
~~~java
@SpringBootApplication(scanBasePackages = {"com.lym.demo"})
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
~~~

## 配置数据源
采用编程式配置多数据源
### 配置数据源1
~~~java
@Configuration
@MapperScan(basePackages = "com.lym.demo.db1.mapper", sqlSessionTemplateRef = "db1SqlSessionTemplate")
public class DB1Config {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    @Primary
    public DataSource db1DataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:com/lym/demo/db1/mapper/*Mapper.xml"));
        return bean.getObject();
    }

    @Bean
    @Primary
    public DataSourceTransactionManager db1TransactionManager(@Qualifier("db1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    // 注意: 此处不能 设置 @Primary，否则@Transactional注解指定事务管理器不生效
//    @Primary
    public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
~~~
@Primary: 注解用来标识当前数据源为主数据源（或默认数据源），但是需要注意，SqlSessionTemplate 不能使用@Primary 注解，否则在使用@Transactional(value="TransactionManager")指定事务管理器时不生效。若@Transactional 不指定事务管理器，默认使用 db1TransactionManager
classpath*:com/lym/demo/db1/mapper/*Mapper.xml : 指定当前数据源管理的Mapper.xml
### 配置数据源2
~~~java
@Configuration
@MapperScan(basePackages = "com.lym.demo.db2.mapper", sqlSessionTemplateRef = "db2SqlSessionTemplate")
public class DB2Config {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource db2DataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory db2SqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:com/lym/demo/db2/mapper/*Mapper.xml"));
        return bean.getObject();
    }

    @Bean
    public DataSourceTransactionManager db2TransactionManager(@Qualifier("db2DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate db2SqlSessionTemplate(@Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
~~~
db2: 是从数据源，所以不需要@Primary 修饰，当使用该数据源的事务时，直接通过@Transactional(value="db2TransactionManager")指定。

注意: 多个数据源之间加载的xml文件不能再同一个包中。

## 示例
### 1、测试多数据源查询数据
~~~java
public List<User> getAllList() {
    List<User1Info> user1InfoList = user1Dao.getList();
    List<User2Info> user2InfoList = user2Dao.getList();
}
~~~
本示例可以同时从两个数据源中获取数据。
### 2、默认事务管理
~~~java
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
public void insert(String username, Integer gender) {
    user1Dao.insert(username, gender);
    user2Dao.insert(username, gender);
    // 抛出运行时异常
    throw new RuntimeException("抛出异常");
}
~~~
最终结果，user1插入失败，user2插入成功。user1为主数据源。
### 3、指定事务管理器
~~~java
@Transactional(value = "db2TransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
public void insert(String username, Integer gender) {
    user1Dao.insert(username, gender);
    user2Dao.insert(username, gender);
    // 抛出运行时异常
    throw new RuntimeException("抛出异常");
}
~~~
最终结果，user1插入成功，user2插入失败。使用的是user2数据源的事务管理器。
