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


}