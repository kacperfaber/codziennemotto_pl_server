package pl.codziennemotto.controllers.textset

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.codziennemotto.controllers.ControllerBase
import pl.codziennemotto.data.dto.JoinLink
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.Text
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.security.Authenticated
import pl.codziennemotto.services.joinlink.JoinLinkService
import pl.codziennemotto.services.summary.SummaryService
import pl.codziennemotto.services.text.TextService
import pl.codziennemotto.services.user.UserService
import java.time.LocalDate

@RestController
@RequestMapping("text-set")
@Authenticated
class TextSetController(
        userService: UserService,
        private val textService: TextService,
        private val joinLinkService: JoinLinkService,
        private val summaryService: SummaryService
) : ControllerBase(userService) {
    @PostMapping("{id}/join-with-code/{code}")
    fun joinWithCodeEndpoint(@PathVariable id: Int, @PathVariable code: String): ResponseEntity<Reader?> {
        val result = textService.joinWithCode(id, user!!, code)

        return when (result.result) {
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

    @GetMapping("{id}/texts/all/visible")
    fun allVisibleTextsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>> = of(textService.getAllVisibleTexts(id, user!!))

    @GetMapping("{id}/texts/past")
    fun pastTextsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>> = of(textService.getPastTexts(id, user!!))

    class AddTextPayload {
        lateinit var text: String

        @JsonFormat(pattern = "yyyy-MM-dd")
        var date: LocalDate? = null
        var order: Int = 0
    }

    @PutMapping("{id}/add")
    fun addTextByIdEndpoint(@PathVariable id: Int, @RequestBody payload: AddTextPayload): ResponseEntity<Text> =
            of(textService.addText(id, user!!, payload.text, payload.date, payload.order))

    class CreateNewTextSetPayload {
        lateinit var title: String
        lateinit var description: String
    }

    @PostMapping("/create-new")
    fun createNewTextSetEndpoint(@RequestBody payload: CreateNewTextSetPayload): ResponseEntity<TextSet> =
            of(textService.createNewTextSet(user!!, payload.title, payload.description))

    @PostMapping("{id}/create-join-link")
    fun joinLinkEndpoint(@PathVariable id: Int): ResponseEntity<JoinLink> = of(joinLinkService.createJoinLink(id, user!!))

    @DeleteMapping("{setId}/{textId}")
    fun deleteTextByIdEndpoint(@PathVariable setId: Int, @PathVariable textId: Int): ResponseEntity<Boolean> =
            ofBoolean(textService.deleteText(setId, textId, user!!))

    @GetMapping("where-i-am-owner")
    fun setsIAmOwnerEndpoint(): ResponseEntity<List<TextSet>> = of(textService.getAllByOwner(user!!))

    @GetMapping("where-i-am-reader")
    fun setsIAmReaderEndpoint(): ResponseEntity<List<TextSet>> = of(textService.getAllByReader(user!!))

    @GetMapping("summary")
    fun summaryEndpoint(): ResponseEntity<List<SummaryService.SummaryObject>> = ok(summaryService.createSummaryFor(user!!))

    @GetMapping("{setId}/readers/include-users")
    fun readersIncludeUsersEndpoint(@PathVariable setId: Int): ResponseEntity<Iterable<TextService.ReaderIncludeUser>> = of(textService.getReadersIncludeUsers(user!!, setId))
}