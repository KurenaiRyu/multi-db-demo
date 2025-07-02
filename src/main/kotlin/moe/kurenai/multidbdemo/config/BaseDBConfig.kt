package moe.kurenai.multidbdemo.config

import com.atomikos.spring.AtomikosDataSourceBean
import moe.kurenai.multidbdemo.entity.Loan
import moe.kurenai.multidbdemo.repository.base.BookRepository
import org.postgresql.xa.PGXADataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import javax.sql.DataSource

@EnableJpaRepositories(
    basePackageClasses = [BookRepository::class],
    entityManagerFactoryRef = "baseEMF",
)
@Configuration
class BaseDBConfig {


    @Bean("baseDS")
    fun baseDS(): DataSource {
        val xaDs = PGXADataSource().apply {
            setUrl("jdbc:postgresql://localhost:15432/base_db")
        }
        return AtomikosDataSourceBean().apply {
            xaDataSource = xaDs
            uniqueResourceName = "baseDS"
        }
    }

    @Bean("baseEMF")
    fun baseEntityManagerFactory(builder: EntityManagerFactoryBuilder, @Qualifier("baseDS") ds: DataSource): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(ds)
            .jta(true)
            .packages(Loan::class.java.packageName)
            .persistenceUnit("basePersistenceUnit")
            .build()
    }
}