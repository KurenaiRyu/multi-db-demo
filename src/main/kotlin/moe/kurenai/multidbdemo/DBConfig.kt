package moe.kurenai.multidbdemo

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import moe.kurenai.multidbdemo.entity.Loan
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
class DBConfig {

    @Bean
    @Primary
    fun routingDataSource(): DataSource {
        val ds = RoutingDataSource()
        val cluster1DS = cluster1DS()
        val targetDataSources: Map<Any, Any> = mutableMapOf(
            Cluster.CLUSTER1.clusterName to cluster1DS,
            Cluster.CLUSTER2.clusterName to cluster2DS(),
        )
        ds.setTargetDataSources(targetDataSources)
        ds.setDefaultTargetDataSource(cluster1DS)

        return ds
    }

    @Bean
    fun entityManagerFactory(builder: EntityManagerFactoryBuilder, ds: DataSource): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(ds)
            .packages(Loan::class.java.packageName)
            .build()
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    @Bean("cluster1DS")
    fun cluster1DS(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/multi_db_test?currentSchema=cluster_1"
//            username = "kurenai"
//            password = "test"
        }
    }

    @Bean("cluster2DS")
    fun cluster2DS(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/multi_db_test?currentSchema=cluster_2"
//            username = "kurenai"
//            password = "test"
        }
    }

}