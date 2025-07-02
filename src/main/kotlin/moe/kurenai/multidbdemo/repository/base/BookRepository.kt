package moe.kurenai.multidbdemo.repository.base

import moe.kurenai.multidbdemo.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("bookRepository")
interface BookRepository: JpaRepository<Book, Int>