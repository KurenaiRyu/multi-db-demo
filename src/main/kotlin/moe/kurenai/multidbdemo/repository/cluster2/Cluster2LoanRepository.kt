package moe.kurenai.multidbdemo.repository.cluster2

import moe.kurenai.multidbdemo.repository.common.LoanRepository
import org.springframework.stereotype.Repository

@Repository("cluster2LoanRepository")
interface Cluster2LoanRepository : LoanRepository