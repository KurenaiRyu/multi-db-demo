package moe.kurenai.multidbdemo

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource


@Configuration
class DBConfig {

    @Bean("cluster1DS")
    @Primary
    @ConfigurationProperties("spring.application.datasource.cluster1")
    fun cluster1DS(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build().apply {
            jdbcUrl = "jdbc:postgresql://10.0.0.20:5432/multi_db_test?currentSchema=cluster_1"
            username = "kurenai"
            password = "test"
        }
    }

    @Bean("cluster2DS")
    @ConfigurationProperties("spring.application.datasource.cluster2")
    fun cluster2DS(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build().apply {
            jdbcUrl = "jdbc:postgresql://10.0.0.20:5432/multi_db_test?currentSchema=cluster_2"
            username = "kurenai"
            password = "test"
        }
    }

}