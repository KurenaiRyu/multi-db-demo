package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManagerFactory
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean

class DynamicEntityManagerFactorBean(
    val map: MutableMap<String, LocalContainerEntityManagerFactoryBean>
): AbstractEntityManagerFactoryBean() {

    override fun getNativeEntityManagerFactory(): EntityManagerFactory {
        return map.getOrDefault(RoutingDataSource.getCurrentCatalog(), map.values.first()).nativeEntityManagerFactory
    }

    override fun createNativeEntityManagerFactory(): EntityManagerFactory {
        return nativeEntityManagerFactory
    }
}