package pl.codziennemotto.data.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate

@Entity
@Table(name = "text")
open class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int? = null

    open lateinit var text: String

    open var date: LocalDate? = null

    open var order: Int = 0

    open var shown: LocalDate? = null

    @JoinColumn(name = "text_set_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    open lateinit var textSet: TextSet

    @Column(name = "text_set_id", insertable = false, updatable = false)
    open var textSetId: Int? = null
}