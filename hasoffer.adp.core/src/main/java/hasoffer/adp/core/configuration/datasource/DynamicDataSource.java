package hasoffer.adp.core.configuration.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by chevy on 2016/11/22.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // 对应 key
        //  <entry value-ref="readDataSource" key="read"></entry> <!-- key -->
//        return "read";
//        return "master";
        return DataSourceContextHolder.getDataSourceType();
    }

}
