package moe.kurenai.multidbdemo

import jakarta.persistence.EntityManager
import org.springframework.data.jpa.domain.Specification

object TxUtil {

    inline fun <reified T> doWith(em: EntityManager, spec: Specification<T>) {
        val cb = em.criteriaBuilder
        val query = cb.createQuery(T::class.java)
        val root = query.from(T::class.java)
        em.createQuery(query.where(spec.toPredicate(root, query, cb)))
    }

}