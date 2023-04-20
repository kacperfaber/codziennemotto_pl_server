import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.GenericGenerator

@MappedSuperclass
open class BaseDto {
    @Id
    @GenericGenerator(name = "id_generator", strategy = "pl.codziennemotto.data.IdGenerator")
    @GeneratedValue(generator = "id_generator")
    open var id: Int = 0
}