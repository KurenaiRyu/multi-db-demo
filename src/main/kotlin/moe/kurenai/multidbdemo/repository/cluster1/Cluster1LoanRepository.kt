package moe.kurenai.multidbdemo.repository.cluster1

import moe.kurenai.multidbdemo.repository.common.LoanRepository
import org.springframework.stereotype.Repository

@Repository("cluster1LoanRepository")
interface Cluster1LoanRepository : LoanRepository {

}