package moe.kurenai.multidbdemo.config

import com.atomikos.spring.AtomikosDataSourceBean
import moe.kurenai.multidbdemo.entity.Loan
import moe.kurenai.multidbdemo.repository.common.LoanRepository
import moe.kurenai.multidbdemo.spring.DynamicEntityManagerFactoryBean
import moe.kurenai.multidbdemo.spring.RoutingDataSource
import org.postgresql.xa.PGXADataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackageClasses = [LoanRepository::class],
)
class DBConfig {

    @Bean
    @Primary
    fun routingDataSource(
        @Qualifier("cluster1DS") c1DS: DataSource,
        @Qualifier("cluster2DS") c2DS: DataSource
    ): DataSource {
        val ds = RoutingDataSource()
        val targetDataSources: Map<Any, Any> = mutableMapOf(
            Cluster.CLUSTER1.clusterName to c1DS,
            Cluster.CLUSTER2.clusterName to c2DS,
        )
        ds.setTargetDataSources(targetDataSources)
        ds.setDefaultTargetDataSource(c1DS)

        return ds
    }

    @Bean("cluster1DS")
    fun cluster1DS(): DataSource {
        val xaDs = PGXADataSource().apply {
            setUrl("jdbc:postgresql://localhost:5432/multi_db_test?currentSchema=cluster_1")
        }
        return AtomikosDataSourceBean().apply {
            xaDataSource = xaDs
            uniqueResourceName = "cluster1DS"
        }
    }

    @Bean("cluster1EMF")
    fun cluster1EntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("cluster1DS") ds: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(ds)
            .jta(true)
            .packages(Loan::class.java.packageName)
            .persistenceUnit("cluster1PersistenceUnit")
            .build()
    }

    @Bean("cluster2DS")
    fun cluster2DS(): DataSource {
        val xaDs = PGXADataSource().apply {
            setUrl("jdbc:postgresql://localhost:5432/multi_db_test?currentSchema=cluster_2")
        }
        return AtomikosDataSourceBean().apply {
            xaDataSource = xaDs
            uniqueResourceName = "cluster2DS"
        }
    }

    @Bean("cluster2EMF")
    fun cluster2EntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("cluster2DS") ds: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(ds)
            .jta(true)
            .packages(Loan::class.java.packageName)
            .persistenceUnit("cluster2PersistenceUnit")
            .build()
    }

    @Bean
    @Primary
    fun entityManagerFactory(
        @Qualifier("cluster1EMF") cluster1EMF: LocalContainerEntityManagerFactoryBean,
        @Qualifier("cluster2EMF") cluster2EMF: LocalContainerEntityManagerFactoryBean,
    ): AbstractEntityManagerFactoryBean {
        val manager = DynamicEntityManagerFactoryBean(
            mapOf(
                "cluster1" to cluster1EMF,
                "cluster2" to cluster2EMF
            )
        )
        return manager
    }

}