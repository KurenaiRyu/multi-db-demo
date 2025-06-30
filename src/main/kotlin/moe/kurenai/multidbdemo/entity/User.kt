package moe.kurenai.multidbdemo.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serial
import java.io.Serializable

@Entity
@Table(name = "user")
class User(
    @Id
    var id: Int? = null,
    var name: String
) : Serializable {

    companion object {
        @Serial
        private val serialVersionUID = 1L
    }
}
