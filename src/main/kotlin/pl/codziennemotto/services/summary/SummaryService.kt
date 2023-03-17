package pl.codziennemotto.services.summary

import org.springframework.stereotype.Component
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.data.dto.User
import pl.codziennemotto.services.text.TextService

@Component
class SummaryService(private val textService: TextService) {

    inner class SummaryObject(val textSet: TextSet, val text: String?)

    fun createSummaryFor(user: User): List<SummaryObject> {
        val textSets = textService.getAllByUser(user)
        return textSets.map {
            SummaryObject(it, textService.getDailyText(it))
        }
    }
}