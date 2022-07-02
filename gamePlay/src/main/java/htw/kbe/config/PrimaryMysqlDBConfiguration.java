/*
package htw.kbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("!test")          // 1
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "au.com.myblog.dao", entityManagerFactoryRef = "entityManager", transactionManagerRef = "transactionManager")        // 2
public class PrimaryMysqlDBConfiguration {
    @Bean(name = "dataSource")      // 3
    @Primary
    @ConfigurationProperties(prefix = "primary.datasource.mysql")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }
    @PersistenceContext(unitName = "primary")   // 4
    @Primary
    @Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean mySqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(mysqlDataSource()).persistenceUnit("primary").properties(jpaProperties())
                .packages("au.com.myblog.domain").build();
    }
    private Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.ejb.naming_strategy", new SpringNamingStrategy());
        return props;
    }
}
*/
