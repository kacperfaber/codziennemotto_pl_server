package pl.codziennemotto.controllers.textset

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.codziennemotto.controllers.ControllerBase
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.security.Authenticated
import pl.codziennemotto.services.text.TextService
import pl.codziennemotto.services.user.UserService

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
}