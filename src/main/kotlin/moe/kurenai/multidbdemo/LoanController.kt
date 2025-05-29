package moe.kurenai.multidbdemo

import moe.kurenai.multidbdemo.entity.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

    @PostMapping("multiRepo")
    fun multiRepo() {
        service.multiRepo()
    }

    @PostMapping("single")
    fun single() {
        catalogService.changeCatalog(Cluster.CLUSTER1)
        service.testSingle()
        catalogService.changeCatalog(Cluster.CLUSTER2)
        service.testSingle()
    }


}