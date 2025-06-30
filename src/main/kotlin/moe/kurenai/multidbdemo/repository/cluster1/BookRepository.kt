package moe.kurenai.multidbdemo.repository.cluster1

import moe.kurenai.multidbdemo.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("loanRepository")
interface BookRepository: JpaRepository<Book, Int>