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

    fun changeCatalog(cluster: Cluster) {
        RoutingDataSource.setCurrentCatalog(cluster.clusterName)
    }

}