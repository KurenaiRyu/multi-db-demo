package moe.kurenai.multidbdemo

import moe.kurenai.multidbdemo.config.Cluster
import moe.kurenai.multidbdemo.spring.RoutingDataSource
import org.springframework.stereotype.Service

@Service
class CatalogService {

    fun changeCatalog(cluster: Cluster) {
        RoutingDataSource.setCurrentCatalog(cluster.clusterName)
    }

}