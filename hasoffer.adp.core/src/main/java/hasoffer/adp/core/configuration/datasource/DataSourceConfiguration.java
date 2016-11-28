package hasoffer.adp.core.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Descripton of DataSourceConfiguration
 * @since 1.0
 * @datetime 2016-11-24 20:17:15
 * @author Scarecroweib <weib_tion@126.com>
 */
@Configuration
@PropertySource("classpath:/jdbc.properties") 
@MapperScan(basePackages="hasoffer.adp.core.dao")
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableTransactionManagement
public class DataSourceConfiguration {
    @Autowired  
    Environment env; 

    /**
     * Master数据源定义
     * @return DataSource <b>interface</b>
     * @throws SQLException 
     */
    @Bean("DataSource-master")
    public DataSource MasterDataSource() throws SQLException{
        DruidDataSource ds = new DruidDataSource();

        //基本属性 url、user、password
        ds.setUrl(env.getProperty("master.url"));
        ds.setUsername(env.getProperty("master.username"));
        ds.setPassword(env.getProperty("master.password"));

        //配置初始化大小、最小、最大
        ds.setInitialSize(20);
        ds.setMinIdle(20);
        ds.setMaxActive(100);

        //配置获取连接等待超时的时间
        ds.setMaxWait(60000);

        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        ds.setTimeBetweenEvictionRunsMillis(60000);

        //配置一个连接在池中最小生存的时间，单位是毫秒
        ds.setMinEvictableIdleTimeMillis(300000);

        ds.setValidationQuery("SELECT 'x'");
        ds.setTestWhileIdle(true);
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(false);

        //打开PSCache，并且指定每个连接上PSCache的大小
        ds.setPoolPreparedStatements(true);
        ds.setMaxPoolPreparedStatementPerConnectionSize(20);

        //配置监控统计拦截的filters
        ds.setFilters("stat");

        return ds;
    }

    /**
     * Slave数据源定义
     * @return DataSource <b>interface</b>
     * @throws SQLException 
     */
    @Bean("DataSource-slave")
    public DataSource SlaveDataSource() throws SQLException{
        DruidDataSource ds = new DruidDataSource();

        //基本属性 url、user、password
        ds.setUrl(env.getProperty("slave.url"));
        ds.setUsername(env.getProperty("slave.username"));
        ds.setPassword(env.getProperty("slave.password"));

        //配置初始化大小、最小、最大
        ds.setInitialSize(20);
        ds.setMinIdle(20);
        ds.setMaxActive(100);

        //配置获取连接等待超时的时间
        ds.setMaxWait(60000);

        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        ds.setTimeBetweenEvictionRunsMillis(60000);

        //配置一个连接在池中最小生存的时间，单位是毫秒
        ds.setMinEvictableIdleTimeMillis(300000);

        ds.setValidationQuery("SELECT 'x'");
        ds.setTestWhileIdle(true);
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(false);

        //打开PSCache，并且指定每个连接上PSCache的大小
        ds.setPoolPreparedStatements(true);
        ds.setMaxPoolPreparedStatementPerConnectionSize(20);

        //配置监控统计拦截的filters
        ds.setFilters("stat");
        return ds;
    }

    /**
     * 统合多个数据源
     * @return DynamicDataSource <b>extends</b> AbstractRoutingDataSource
     * @throws SQLException 
     */
    @Bean("datasource")
    public DynamicDataSource GetDataSource() throws SQLException{
        DynamicDataSource ds = new DynamicDataSource();

        Map<Object, Object> sourceMap = new HashMap();
        sourceMap.put("master", MasterDataSource());
        sourceMap.put("slave", SlaveDataSource());
        ds.setTargetDataSources(sourceMap);

        ds.setDefaultTargetDataSource(MasterDataSource());

        return ds;
    }

    /**
     * 通过数据源创建SqlSessionFactoryBean
     * @param dataSource <b>autowired</b>
     * @return 
     */
    @Bean("SqlSessionFactory")
    public SqlSessionFactoryBean MasterSessionFactory(DynamicDataSource dataSource){
        SqlSessionFactoryBean sf = new SqlSessionFactoryBean();
        sf.setDataSource(dataSource);
        return sf;
    }
    
    /**
     * 注册事务管理器
     * 使其生效 @EnableTransactionManagement
     * @param dataSource
     * @return 
     */
    @Bean("txManager")
    public DataSourceTransactionManager txManager(DynamicDataSource dataSource){
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource);
        return txManager;
    }
    
    /**
     * 数据源选择器（通知Advice）
     * @return 
     */
    @Bean("dataSourceSelectAdvice")
    public DataSourceSelectAdvice dataSourceSelectAdvice(){
        return new DataSourceSelectAdvice();
    }
    
    /**
     * 创建使用AspectJ表达式作为Pointcut的通知者Advisor
     * 代理注释 @EnableAspectJAutoProxy(proxyTargetClass=true)
     * @param advice <b>autowired</b>
     * @return 
     */
    @Bean("dataSourceSelectAdvisor")
    public AspectJExpressionPointcutAdvisor dataSourceSelectAdvisor(DataSourceSelectAdvice advice){
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setAdvice(advice);
        advisor.setExpression("execution(* ashare.core..*(..))");
        advisor.setOrder(1);
        return advisor;
    }
}
