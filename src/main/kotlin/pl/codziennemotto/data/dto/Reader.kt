package pl.codziennemotto.data.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "reader")
open class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int? = null

    @JoinColumn(name = "text_set_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    open lateinit var textSet: TextSet

    @Column(name = "text_set_id", insertable = false, updatable = false)
    open var textSetId: Int? = null

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    open lateinit var user: User

    @Column(name = "user_id", insertable = false, updatable = false)
    open var userId: Int? = null
}