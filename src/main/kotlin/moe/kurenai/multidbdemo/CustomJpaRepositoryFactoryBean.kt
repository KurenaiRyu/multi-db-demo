package moe.kurenai.multidbdemo

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import javax.persistence.EntityManager

class CustomJpaRepositoryFactoryBean<T: Repository<S, ID>, S, ID>(repositoryInterface: Class<T>) :
    JpaRepositoryFactoryBean<T, S, ID>(repositoryInterface) {

    fun createFactory(entityManager: EntityManager): RepositoryFactorySupport {
        return super.createRepositoryFactory(entityManager)
    }

}