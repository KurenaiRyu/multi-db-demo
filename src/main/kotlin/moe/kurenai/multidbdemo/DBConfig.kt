package moe.kurenai.multidbdemo

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import moe.kurenai.multidbdemo.entity.Loan
import moe.kurenai.multidbdemo.repository.base.LoanRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.transaction.ChainedTransactionManager
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackageClasses = [LoanRepository::class],
    entityManagerFactoryRef = "dynamicEMF",
    transactionManagerRef = "dynamicTXM"
)
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
    @Primary
    fun entityManagerFactory(builder: EntityManagerFactoryBuilder, ds: DataSource): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(ds)
            .packages(Loan::class.java.packageName)
            .build()
    }

    @Bean
    @Primary
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

    @Bean("cluster1EMF")
    fun cluster1EntityManagerFactory(builder: EntityManagerFactoryBuilder, @Qualifier("cluster1DS") ds: DataSource): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(ds)
            .packages(Loan::class.java.packageName)
            .build()
    }

    @Bean("cluster1TXM")
    fun cluster1TransactionManager(@Qualifier("cluster1EMF") entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    @Bean("cluster2DS")
    fun cluster2DS(): DataSource {
        return DataSourceBuilder.create().type(HikariDataSource::class.java).build().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/multi_db_test?currentSchema=cluster_2"
//            username = "kurenai"
//            password = "test"
        }
    }

    @Bean("cluster2EMF")
    fun cluster2EntityManagerFactory(builder: EntityManagerFactoryBuilder, @Qualifier("cluster2DS") ds: DataSource): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(ds)
            .packages(Loan::class.java.packageName)
            .build()
    }

    @Bean("cluster2TXM")
    fun cluster2TransactionManager(@Qualifier("cluster2EMF") entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    @Bean("chainedTXM")
    fun chainedTXManager(@Qualifier("cluster1TXM") cluster1TXM: PlatformTransactionManager, @Qualifier("cluster2TXM") cluster2TXM: PlatformTransactionManager): PlatformTransactionManager {
        return ChainedTransactionManager(cluster1TXM, cluster2TXM)
    }

    @Bean("dynamicEMF")
    fun dynamicEntityManagerFactory (builder: EntityManagerFactoryBuilder): AbstractEntityManagerFactoryBean {
        val manager = DynamicEntityManagerFactoryBean(mapOf(
            "cluster1" to cluster1EntityManagerFactory(builder, cluster1DS()),
            "cluster2" to cluster2EntityManagerFactory(builder, cluster2DS())))
        return manager
    }

    @Bean("dynamicTXM")
    fun dynamicTransactionManager(@Qualifier("dynamicEMF") entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }






//    @Bean("chainedTXM")
//    fun chainedTXManager(transactionManager: PlatformTransactionManager): PlatformTransactionManager {
//        return ChainedTransactionManager(tx)
//    }

}