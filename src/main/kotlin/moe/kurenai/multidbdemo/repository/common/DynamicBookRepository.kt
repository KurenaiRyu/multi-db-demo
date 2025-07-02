package moe.kurenai.multidbdemo.repository.common

import moe.kurenai.multidbdemo.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("dynamicBookRepository")
interface DynamicBookRepository: JpaRepository<Book, Int>