package moe.kurenai.multidbdemo

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import moe.kurenai.multidbdemo.entity.Loan
import moe.kurenai.multidbdemo.repository.LoanRepository
import moe.kurenai.multidbdemo.repository.cluster1.Cluster1LoanRepository
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@EnableJpaRepositories(
    basePackageClasses = [Cluster1LoanRepository::class],
    entityManagerFactoryRef = "cluster1EMF",
    transactionManagerRef = "cluster1TXM",

)
@Configuration
class Cluster1DBConfig {


}