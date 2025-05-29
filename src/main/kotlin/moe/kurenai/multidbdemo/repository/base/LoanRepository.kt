package moe.kurenai.multidbdemo.repository.base

import moe.kurenai.multidbdemo.entity.Loan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("loanRepository")
interface LoanRepository: JpaRepository<Loan, Int>