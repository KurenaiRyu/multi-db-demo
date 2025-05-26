package moe.kurenai.multidbdemo

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import moe.kurenai.multidbdemo.entity.Loan
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

class Cluster1DBConfig {

    @Bean("cluster1DS")
    @Primary
    fun cluster1DS(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/multi_db_test?currentSchema=cluster_1"
//            username = "kurenai"
//            password = "test"
        }
    }

    @Bean("cluster1EMF")
    @Primary
    fun entityManagerFactoryBean(builder: EntityManagerFactoryBuilder, ds: DataSource): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(ds)
            .packages(Loan::class.java.packageName)
            .build()
    }

    @Bean("cluster1TXM")
    @Primary
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

}