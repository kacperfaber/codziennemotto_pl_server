package pl.codziennemotto.controllers.textset

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.codziennemotto.controllers.ControllerBase
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.Text
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.security.Authenticated
import pl.codziennemotto.services.text.TextService
import pl.codziennemotto.services.user.UserService
import java.time.LocalDate

@RestController
@RequestMapping("text-set")
@Authenticated
class TextSetController(userService: UserService, private val textService: TextService) : ControllerBase(userService) {
    @PostMapping("{id}/join-with-code/{code}")
    fun joinWithCodeEndpoint(@PathVariable id: Int, @PathVariable code: String): ResponseEntity<Reader?> {
        val result = textService.joinWithCode(id, user!!, code)

        return when(result.result) {
            TextService.JoinWithCodeResult.OK -> ok(result.reader)
            else -> badRequest()
        }
    }

    @GetMapping("{id}")
    fun textSetByIdEndpoint(@PathVariable id: Int): ResponseEntity<TextSet> = of(textService.getTextSet(id, user!!))

    @GetMapping("{id}/readers")
    fun readersByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Reader>> = of(textService.getTextSetReaders(id, user!!))

    @GetMapping("{id}/texts/all")
    fun textsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>> = of(textService.getAllTexts(id, user!!))

    @GetMapping("{id}/texts/past")
    fun pastTextsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>> = of(textService.getPastTexts(id, user!!))
}