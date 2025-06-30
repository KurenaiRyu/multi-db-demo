package moe.kurenai.multidbdemo.entity

import java.io.Serial
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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
