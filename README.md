使用方法：

主pom.xml配置一下plugin属性

1、以下配置基于oracle数据库，以及mybatis的1.3.5版本
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.5</version>
    <configuration>
        <verbose>true</verbose>
        <overwrite>true</overwrite>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.3.5</version>
        </dependency>
        <!--引入数据库驱动，可以在generatorConfig中省去配置项 -->
        <dependency>
            <groupId>com.evun.cloudmechanics</groupId>
            <artifactId>Oracle_10g_classes12</artifactId>
            <version>1.0.0</version>
        </dependency>
        <!--把自己的插件引入到MBG Maven Plugin的依赖中 -->
        <dependency>
            <groupId>com.github.boyxie</groupId>
            <artifactId>mybatis-plugins</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</plugin>

2、在resource下新增generatorConfig.xml，具体配置看示列代码

3、运行，可以直接maven插件运行，其他方法看官方文档或以下参考博客

参考文章包括：
http://www.mybatis.org/generator/configreference/xmlconfig.html
http://blog.csdn.net/qq_21251983/article/details/50731368
http://www.jianshu.com/p/1b826d43dbaf 系列
http://blog.csdn.net/isea533/article/details/52430691
