package moe.kurenai.multidbdemo

import moe.kurenai.multidbdemo.repository.cluster2.Cluster2LoanRepository
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(
    basePackageClasses = [Cluster2LoanRepository::class],
    entityManagerFactoryRef = "cluster2EMF",
    transactionManagerRef = "cluster2TXM",
)
@Configuration
class Cluster2DBConfig {

}