package moe.kurenai.multidbdemo

import moe.kurenai.multidbdemo.config.Cluster
import moe.kurenai.multidbdemo.entity.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/loan")
class LoanController {

    @Autowired
    private lateinit var service: LoanService

    @Autowired
    private lateinit var catalogService: CatalogService

    @GetMapping("{cluster}/user/{userId}")
    fun findByUserId(@PathVariable("cluster") cluster: Cluster, @PathVariable("userId") userId: Long): List<Loan> {
        return service.findByUserId(cluster,userId)
    }

    @PostMapping("{cluster}/lean/{userId}/{bookId}")
    fun findByUserId(@PathVariable("cluster") cluster: Cluster,
                     @PathVariable("userId") userId: Int,
                     @PathVariable("bookId") bookId: Int) {
        service.lean(cluster,userId, bookId)
    }

    @GetMapping("test")
    fun testRetrieve(): List<Loan> {
        return service.testRetrieve()
    }

    @PostMapping("test")
    fun testSave() {
        service.testSave()
    }

    @PostMapping("rollback")
    fun testRollback() {
        service.testRollback()
    }

    @PostMapping("single")
    fun single() {
        catalogService.changeCatalog(Cluster.CLUSTER1)
        service.testSingle()
        catalogService.changeCatalog(Cluster.CLUSTER2)
        service.testSingle()
    }

    @GetMapping("xa")
    fun xa() {
        catalogService.changeCatalog(Cluster.CLUSTER1)
        service.xa()
        catalogService.changeCatalog(Cluster.CLUSTER2)
        service.xa()
    }

    @GetMapping("xa-roll-back")
    fun xaRollBack() {
        catalogService.changeCatalog(Cluster.CLUSTER1)
        service.xaRollBack()
        catalogService.changeCatalog(Cluster.CLUSTER2)
        service.xaRollBack()
    }

    @GetMapping("book")
    fun book() {
        catalogService.changeCatalog(Cluster.CLUSTER1)
        service.book()
    }


}