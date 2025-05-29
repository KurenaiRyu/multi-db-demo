package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.spi.PersistenceProvider
import jakarta.persistence.spi.PersistenceUnitInfo
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean
import org.springframework.orm.jpa.EntityManagerFactoryInfo
import org.springframework.orm.jpa.JpaDialect
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import javax.sql.DataSource

class DynamicEntityManagerFactoryBean(
    private val entityManagerFactoryMap: Map<String, LocalContainerEntityManagerFactoryBean>
): AbstractEntityManagerFactoryBean() {

    //    override fun getPersistenceProvider(): PersistenceProvider? {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.persistenceProvider
//    }
//
//    override fun getPersistenceUnitInfo(): PersistenceUnitInfo? {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.persistenceUnitInfo
//    }
//
//    override fun getPersistenceUnitName(): String? {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.persistenceUnitName
//    }
//
//    override fun getDataSource(): DataSource? {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.dataSource
//    }
//
//    override fun getEntityManagerInterface(): Class<out EntityManager>? {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.entityManagerInterface
//    }
//
//    override fun getJpaDialect(): JpaDialect? {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.jpaDialect
//    }
//
//    override fun getBeanClassLoader(): ClassLoader {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.beanClassLoader
//    }
//
//    override fun getNativeEntityManagerFactory(): EntityManagerFactory {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.nativeEntityManagerFactory
//    }
//
//    override fun createNativeEntityManager(properties: MutableMap<*, *>?): EntityManager {
//        return entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()]!!.createNativeEntityManager(properties)
//    }

    override fun createNativeEntityManagerFactory(): EntityManagerFactory {
        return getNativeEntityManagerFactory()
    }

    override fun getNativeEntityManagerFactory(): EntityManagerFactory {
        val bean =
            entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()] ?: entityManagerFactoryMap.values.first()
        return bean.nativeEntityManagerFactory
    }
}