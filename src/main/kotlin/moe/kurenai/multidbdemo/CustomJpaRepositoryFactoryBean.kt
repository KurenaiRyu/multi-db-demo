package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport

class CustomJpaRepositoryFactoryBean<T: Repository<S, ID>, S, ID>(repositoryInterface: Class<out T>) :
    JpaRepositoryFactoryBean<T, S, ID>(repositoryInterface) {

    fun createFactory(entityManager: EntityManager): RepositoryFactorySupport {
        return super.createRepositoryFactory(entityManager)
    }

}