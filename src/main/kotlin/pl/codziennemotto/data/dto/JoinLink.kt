package pl.codziennemotto.data.dto

import BaseDto
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime

@Entity
@Table(name = "join_link")
open class JoinLink : BaseDto() {

    @JoinColumn(name = "text_set_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    open lateinit var textSet: TextSet

    @Column(name = "text_set_id", insertable = false, updatable = false)
    open var textSetId: Int? = null

    @Column(name = "active_until")
    open lateinit var activeUntil: LocalDateTime

    open lateinit var code: String
}

