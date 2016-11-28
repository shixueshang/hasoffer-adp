package hasoffer.adp.core.configuration;

import hasoffer.adp.core.configuration.datasource.DataSourceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DataSourceConfiguration.class)
@ComponentScan("hasoffer.adp.core")
public class CoreConfiguration {

}
