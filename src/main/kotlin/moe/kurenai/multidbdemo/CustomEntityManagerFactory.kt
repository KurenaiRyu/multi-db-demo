package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.EntityNotFoundException
import moe.kurenai.multidbdemo.entity.Loan
import org.hibernate.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class CustomEntityManagerFactory {

    @Autowired
    private lateinit var context: ApplicationContext

    private val entityManagerMap: Map<String, EntityManagerFactory> = mutableMapOf()

    fun createEntityManager(cluster: Cluster): Session {
        val clusterName = cluster.clusterName
        val emf = entityManagerMap[clusterName]?: let {
            val ds = context.getBean(clusterName + "DS", DataSource::class.java)
            val factor = LocalContainerEntityManagerFactoryBean()
            factor.dataSource = ds
            factor.persistenceUnitName = clusterName + "PU"
            factor.jpaVendorAdapter = HibernateJpaVendorAdapter()
            factor.setPackagesToScan(Loan::class.java.packageName)
            factor.afterPropertiesSet()
            factor.nativeEntityManagerFactory
        }?:throw EntityNotFoundException("EntityManager Not Found")

        return emf.createEntityManager().unwrap(Session::class.java)
    }

}