package hasoffer.adp.core.configuration.datasource;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * Created by chevy on 2016/10/19.
 */
public class DataSourceSelectAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
    
    @Override
    public void afterReturning(Object returnValue, Method method,
                               Object[] args, Object target) throws Throwable {
        DataSource ds = method.getAnnotation(DataSource.class);

        if (ds == null) {
            return;
        }

        DataSourceContextHolder.clearDataSourceType();
    }
    
    @Override
    public void before(Method method, Object[] args, Object target)
            throws Throwable {

        DataSource ds = method.getAnnotation(DataSource.class);

        if (ds == null) {
            return;
        }

        System.out.println(String.format("method : %s/%s, datasource : %s", method.getDeclaringClass().getName(), method.getName(), ds.value()));

        if (ds.value() == DataSourceType.Slave) {
            DataSourceContextHolder.setDataSourceType("slave");
        } else {
            DataSourceContextHolder.setDataSourceType("master");
        }
    }

}
