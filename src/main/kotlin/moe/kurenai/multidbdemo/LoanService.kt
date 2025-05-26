package moe.kurenai.multidbdemo

import moe.kurenai.multidbdemo.entity.Loan
import moe.kurenai.multidbdemo.repository.LoanRepository
import org.hibernate.FlushMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class LoanService {

    @Autowired
    private lateinit var emf: CustomEntityManagerFactory

    @Autowired
    private lateinit var catalogService: CatalogService

    @Autowired
    private lateinit var repository: LoanRepository

    fun findByUserId(cluster: Cluster, userId: Long):List<Loan>{
        val em = emf.createEntityManager(cluster)
        val cb = em.criteriaBuilder
        val query = cb.createQuery(Loan::class.java)
        val root = query.from(Loan::class.java)
        query.select(root)
            .where(cb.equal(root.get<Int>(Loan::userId.name), userId))
        return em.createQuery(query).resultList
    }

    fun lean(cluster: Cluster, userId: Int, bookId: Int): Loan {
        val em = emf.createEntityManager(cluster)
        em.hibernateFlushMode = FlushMode.AUTO
        val loan = Loan(userId = userId, bookId =  bookId, loanDateTime = LocalDateTime.now())
        em.transaction.begin()
        em.persist(loan)
        em.transaction.commit()
        return loan
    }

    @Transactional
    fun testRetrieve(): List<Loan> {
        val res = arrayListOf<Loan>()

        catalogService.changeCatalog(Cluster.CLUSTER1)
        res.addAll(repository.findAll())
        catalogService.changeCatalog(Cluster.CLUSTER2)
        res.addAll(repository.findAll())
        return res
    }

    @Transactional
    fun testSave() {
        catalogService.changeCatalog(Cluster.CLUSTER1)
        repository.save(Loan(userId = 1, bookId = 1, loanDateTime = LocalDateTime.now()))

        catalogService.changeCatalog(Cluster.CLUSTER2)
        repository.save(Loan(userId = 2, bookId = 2, loanDateTime = LocalDateTime.now()))
    }

    @Transactional(rollbackFor = [Exception::class])
    fun testRollback() {
        catalogService.changeCatalog(Cluster.CLUSTER1)
        repository.save(Loan(userId = 10, bookId = 10, loanDateTime = LocalDateTime.now()))

        catalogService.changeCatalog(Cluster.CLUSTER2)
        repository.save(Loan(userId = 20, bookId = 20, loanDateTime = LocalDateTime.now()))

        throw Exception("Test rollback")
    }

}