package pl.codziennemotto.data

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import kotlin.random.Random

class IdGenerator : IdentifierGenerator{
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Any {
        return Random.nextInt()
    }
}