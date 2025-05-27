package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CatalogService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    fun changeCatalog(cluster: Cluster) {
        entityManager.flush()
        entityManager.clear()

        val session = entityManager.unwrap(Session::class.java)
        RoutingDataSource.setCurrentCatalog(cluster.clusterName)
        session.doWork {
            it.catalog = cluster.clusterName
        }
    }

}