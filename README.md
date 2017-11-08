## Redisson实现缓存和分布式锁

## 使用框架
> * spring boot + mybatis

##  版本
    spring boot
> * [Spring boot 1.5.8.RELEASE](https://github.com/spring-projects/spring-boot/tree/1.5.x)
   > * [mybatis-spring-boot-starter](https://github.com/mybatis/spring-boot-starter)

    Mybatis
> * [Mybatis 3.4.5](https://github.com/mybatis/mybatis-3/releases)

    Redisson
> * [Redisson 2.10.1](https://github.com/redisson/redisson)

## 项目依赖
> * 见pom.xml文件
## application.yml配置(也可用application.properties配置)
server:
  port: 8080

redis:
  address: redis://127.0.0.1:6379

spring:
    datasource:
        name: wcj
        url: jdbc:mysql://127.0.0.1:3306/wcj?useUnicode=true&characterEncoding=utf-8
        username: root
        password: 123456
        # 使用druid数据源
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        filters: stat
        maxActive: 20
        initialSize: 1
        maxWait: 60000
        minIdle: 1
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxOpenPreparedStatements: 20
mybatis:
    config-location: classpath:mybatis-config.xml
logging:
  level: debug
## Mybatis-config.xml配置
    <configuration>
        <typeAliases>
            <package name="com.wcj.emp.dao"/>
        </typeAliases>
        <mappers>
            <mapper resource="emp/mapper/EmpMapper.xml"/>
        </mappers>
    </configuration>

##缓存
> * 自定义cache管理器RedissonJCacheManager

##分布式锁（目前是单机，后续改主从，或者集群）
> * 分布式锁工具类DistributedRedissionLock
> * redisson支持多种redis连接

            1.SentinelServersConfig（哨兵）
            2.MasterSlaveServersConfig（主从）
            3.SingleServerConfig（单机）
            4.ClusterServersConfig（集群）
            5.ReplicatedServersConfig（一主多从）

## 注意事项：项目依赖的redis服务端需支持eval，建议2.8.x以上的版本
> * [redis 3.2.x 安装](http://jingyan.baidu.com/article/0f5fb099045b056d8334ea97.html)
> * [mysql 5.7.1安装](http://note.youdao.com/noteshare?id=935dfbf5be78de584ebd68ed8320a1bd)

