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

    @GetMapping("{cluster}/user/{userId}")
    public fun findByUserId(@PathVariable("cluster") cluster: Cluster, @PathVariable("userId") userId: Long): List<Loan> {
        return service.findByUserId(cluster,userId)
    }

    @PostMapping("{cluster}/lean/{userId}/{bookId}")
    public fun findByUserId(@PathVariable("cluster") cluster: Cluster,
                            @PathVariable("userId") userId: Int,
                            @PathVariable("bookId") bookId: Int) {
        service.lean(cluster,userId, bookId)
    }


}