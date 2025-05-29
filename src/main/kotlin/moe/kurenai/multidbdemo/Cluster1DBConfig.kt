package moe.kurenai.multidbdemo

import moe.kurenai.multidbdemo.repository.cluster1.Cluster1LoanRepository
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(
    basePackageClasses = [Cluster1LoanRepository::class],
    entityManagerFactoryRef = "cluster1EMF",
    transactionManagerRef = "cluster1TXM",
)
@Configuration
class Cluster1DBConfig {


}