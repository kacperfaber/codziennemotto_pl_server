package pl.codziennemotto.data.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "text_set")
open class TextSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int? = null

    open lateinit var title: String

    open lateinit var description: String

    @OneToMany(mappedBy = "text_set")
    open lateinit var texts: MutableList<Text>

    @OneToMany(mappedBy = "text_set")
    open lateinit var joinLinks: MutableList<JoinLink>

    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    open lateinit var owner: User

    @Column(name = "owner_id", insertable = false, updatable = false)
    open var ownerId: Int? = null

    @OneToMany(mappedBy = "textSet")
    open lateinit var readers: MutableList<Reader>
}