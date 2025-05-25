package moe.kurenai.multidbdemo

import moe.kurenai.multidbdemo.entity.Loan
import org.hibernate.FlushMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LoanService {

    @Autowired
    private lateinit var emf: CustomEntityManagerFactory

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

}