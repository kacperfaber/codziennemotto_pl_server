package pl.codziennemotto.data

import BaseDto
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import kotlin.random.Random

class IdGenerator : IdentifierGenerator{
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Any {
        if (`object` is BaseDto){
            return if (`object`.id == null) Random.nextInt() else `object`.id as Any
        }
        return Random.nextInt()
    }
}