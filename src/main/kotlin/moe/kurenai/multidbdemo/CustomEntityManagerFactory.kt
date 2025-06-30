package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import moe.kurenai.multidbdemo.entity.Loan
import org.hibernate.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Service
class CustomEntityManagerFactory {

    @Autowired
    private lateinit var context: ApplicationContext
    @Autowired
    private lateinit var transactionManagerMap: Map<String, PlatformTransactionManager>

    private val entityManagerFactoryMap: MutableMap<String, EntityManagerFactory> = mutableMapOf()
    private val repositoryFactoryBeanMap: MutableMap<String, CustomJpaRepositoryFactoryBean<Repository<Any, Any>, Any, Any>> = mutableMapOf()
    private val repositoryFactoryMap: MutableMap<String, MutableMap<String, RepositoryFactorySupport>> = mutableMapOf()
    private val repositoryMap: MutableMap<String, MutableMap<String, Repository<*, *>>> = mutableMapOf()
    private val entityManagerMap: MutableMap<String, EntityManager> = mutableMapOf()

    fun getEntityManager(cluster: Cluster): Session {
        val clusterName = cluster.clusterName

        val tx = transactionManagerMap.keys.find { it.startsWith(clusterName) }?.let {
            transactionManagerMap[it]
        }?.let {
            it as? JpaTransactionManager
        }

        tx?.dataSource?.let {
            tx.entityManagerFactory?.createEntityManager()
        }

        return entityManagerMap.computeIfAbsent(clusterName) {
            val emf = entityManagerFactoryMap.computeIfAbsent(clusterName) {
                val ds = context.getBean(clusterName + "DS", DataSource::class.java)
                val factor = LocalContainerEntityManagerFactoryBean()
                factor.dataSource = ds
                factor.persistenceUnitName = clusterName + "PU"
                factor.jpaVendorAdapter = HibernateJpaVendorAdapter()
                factor.setPackagesToScan(Loan::class.java.packageName)
                factor.afterPropertiesSet()
                factor.nativeEntityManagerFactory
            }
            emf.createEntityManager()
        }.unwrap(Session::class.java)
    }

    fun <T: Repository<E, F>, E, F> getRepository(cluster: Cluster, repository: Class<T>): T {
        val map = repositoryMap.computeIfAbsent(repository.name) {
            mutableMapOf(cluster.clusterName to getRepositoryFactory(cluster, repository).getRepository(repository))
        }
        return map.computeIfAbsent(cluster.clusterName) {
            getRepositoryFactory(cluster, repository).getRepository(repository)
        } as T
    }

    fun <T: Repository<E, F>, E, F> getRepositoryFactory(cluster: Cluster, repository: Class<T>): RepositoryFactorySupport {
        val factoryBean = getRepositoryFactoryBean(repository)
        val map =  repositoryFactoryMap.computeIfAbsent(repository.name) { _ ->
            mutableMapOf(cluster.clusterName to factoryBean.createFactory(getEntityManager(cluster)))
        }
        return map.computeIfAbsent(cluster.clusterName) {
            factoryBean.createFactory(getEntityManager(cluster))
        }
    }

    private fun <T: Repository<E, F>, E, F> getRepositoryFactoryBean(repository: Class<T>): CustomJpaRepositoryFactoryBean<Repository<E, F>, E, F> {
        CustomJpaRepositoryFactoryBean(repository)
        return repositoryFactoryBeanMap.computeIfAbsent(repository.name) {
            CustomJpaRepositoryFactoryBean(repository) as CustomJpaRepositoryFactoryBean<Repository<Any, Any>, Any, Any>
        } as CustomJpaRepositoryFactoryBean<Repository<E, F>, E, F>
    }

}