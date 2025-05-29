package moe.kurenai.multidbdemo

import org.springframework.stereotype.Service

@Service
class CatalogService {

    fun changeCatalog(cluster: Cluster) {
        RoutingDataSource.setCurrentCatalog(cluster.clusterName)
    }

}