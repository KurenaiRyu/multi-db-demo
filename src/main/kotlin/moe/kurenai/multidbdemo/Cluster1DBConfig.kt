//package moe.kurenai.multidbdemo
//
//import jakarta.persistence.EntityManagerFactory
//import moe.kurenai.multidbdemo.repository.cluster1.Cluster1LoanRepository
//import org.springframework.beans.factory.annotation.Qualifier
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories
//import org.springframework.orm.jpa.JpaTransactionManager
//import org.springframework.transaction.PlatformTransactionManager
//
//@EnableJpaRepositories(
//    basePackageClasses = [Cluster1LoanRepository::class],
//    entityManagerFactoryRef = "cluster1EMF",
//)
//@Configuration
//class Cluster1DBConfig {
//
//}