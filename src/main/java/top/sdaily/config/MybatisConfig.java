package top.sdaily.config;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import top.sdaily.core.mybatis.interceptor.GenericDaoInterceptor;
import top.sdaily.core.mybatis.interceptor.PageInterceptor;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by soya on 2016/10/29.
 */
@Configuration
@EnableTransactionManagement
public class MybatisConfig implements TransactionManagementConfigurer {

    @Resource(name = "dataSource")
    DataSource dataSource;

    /**
     * 可以通过这个类,详细配置mybatis
     * @return
     */
//    @Bean
//    public org.apache.ibatis.session.Configuration mybatisSetting(){
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//
//        return null;
//    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //bean.setTypeAliasesPackage("top.sdaily.model");

        //分页插件
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("databaseType", "mysql");
        pageInterceptor.setProperties(properties);

        GenericDaoInterceptor genericDaoInterceptor = new GenericDaoInterceptor();

        //添加插件
        bean.setPlugins(new Interceptor[]{pageInterceptor});

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            //设置xml扫描路径
            bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("sqlSessionFactory init fail",e);
        }
    }

    /**
     * 用于实际查询的sql工具,传统dao开发形式可以使用这个,基于mapper代理则不需要注入
     * @param sqlSessionFactory
     * @return
     */
    /*@Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }*/
    /**
     * 事务管理,具体使用在service层加入@Transactional注解
     */
    @Bean(name = "transactionManager")
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
