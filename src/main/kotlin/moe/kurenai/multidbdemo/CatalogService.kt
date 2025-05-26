package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.PersistenceContext
import org.hibernate.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Connection

@Service
class CatalogService {

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    fun changeCatalog(cluster: Cluster) {
        entityManager.flush()
        entityManager.clear()

        val session = entityManager.unwrap(Session::class.java)
        RoutingDataSource.setCurrentCatalog(cluster.clusterName)
        session.doWork {
            println("$cluster: $it")
            it.catalog = cluster.clusterName
        }
    }

}