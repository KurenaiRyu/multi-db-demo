package moe.kurenai.multidbdemo.entity

import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "loan")
class Loan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column var userId: Int,
    var bookId: Int,
    @Column(name = "loan_date_time") var loanDateTime: LocalDateTime? = null,
    @Column(name = "return_date_time") var returnDateTime: LocalDateTime? = null
): Serializable {

    companion object {
        @Serial
        private val serialVersionUID = 1L
    }
}
