package moe.kurenai.multidbdemo.entity

import jakarta.persistence.*
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "book")
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var title: String,
    var author: String,
    var update: LocalDateTime = LocalDateTime.now(),
): Serializable {

    companion object {
        @Serial
        private val serialVersionUID = 1L
    }
}
