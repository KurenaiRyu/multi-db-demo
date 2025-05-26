package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.EntityNotFoundException
import moe.kurenai.multidbdemo.entity.Loan
import org.hibernate.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class CustomEntityManagerFactory {

    @Autowired
    private lateinit var context: ApplicationContext

    private val entityManagerFactoryMap: MutableMap<String, EntityManagerFactory> = mutableMapOf()
    private val repositoryFactoryBeanMap: MutableMap<String, CustomJpaRepositoryFactoryBean<Repository<Any, Any>, Any, Any>> = mutableMapOf()
    private val repositoryFactoryMap: MutableMap<String, MutableMap<String, RepositoryFactorySupport>> = mutableMapOf()
    private val repositoryMap: MutableMap<String, MutableMap<String, JpaRepository<*, *>>> = mutableMapOf()

    fun createEntityManager(cluster: Cluster): Session {
        val clusterName = cluster.clusterName
        val emf = entityManagerFactoryMap[clusterName]?: let {
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

    fun <T: Repository<E, F>, E, F> getRepository(cluster: Cluster, repository: Class<T>): T {
        val factory = getRepositoryFactory(cluster, repository)
        return factory.getRepository(repository)
    }

    fun <T: Repository<E, F>, E, F> getRepositoryFactory(cluster: Cluster, repository: Class<T>): RepositoryFactorySupport {
        val factoryBean = getRepositoryFactoryBean(repository)
        val map =  repositoryFactoryMap.computeIfAbsent(repository.name) { _ ->
            mutableMapOf(cluster.clusterName to factoryBean.createFactory(createEntityManager(cluster)))
        }
        return map.computeIfAbsent(cluster.clusterName) {
            factoryBean.createFactory(createEntityManager(cluster))
        }
    }

    private fun <T: Repository<E, F>, E, F> getRepositoryFactoryBean(repository: Class<T>): CustomJpaRepositoryFactoryBean<Repository<E, F>, E, F> {
        CustomJpaRepositoryFactoryBean(repository)
        return repositoryFactoryBeanMap.computeIfAbsent(repository.name) {
            CustomJpaRepositoryFactoryBean(repository) as CustomJpaRepositoryFactoryBean<Repository<Any, Any>, Any, Any>
        } as CustomJpaRepositoryFactoryBean<Repository<E, F>, E, F>
    }

}