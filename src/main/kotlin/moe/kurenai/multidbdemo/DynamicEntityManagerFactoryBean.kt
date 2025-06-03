package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManagerFactory
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean

class DynamicEntityManagerFactoryBean(
    private val entityManagerFactoryMap: Map<String, LocalContainerEntityManagerFactoryBean>
): AbstractEntityManagerFactoryBean() {

    override fun destroy() {
        for (factoryBean in entityManagerFactoryMap.values) {
            factoryBean.destroy()
        }
    }

    override fun createNativeEntityManagerFactory(): EntityManagerFactory {
        return getNativeEntityManagerFactory()
    }

    override fun getNativeEntityManagerFactory(): EntityManagerFactory {
        val bean =
            entityManagerFactoryMap[RoutingDataSource.getCurrentCatalog()] ?: entityManagerFactoryMap.values.first()
        return bean.nativeEntityManagerFactory
    }
}