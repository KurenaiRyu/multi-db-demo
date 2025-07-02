package moe.kurenai.multidbdemo.spring

import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport

class CustomJpaRepositoryFactoryBean<T: Repository<S, ID>, S, ID>(repositoryInterface: Class<T>) :
    JpaRepositoryFactoryBean<T, S, ID>(repositoryInterface) {

    fun createFactory(entityManager: EntityManager): RepositoryFactorySupport {
        return super.createRepositoryFactory(entityManager)
    }

}