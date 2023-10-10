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
) : ControllerBase(userService), TextSetApi {
    @PostMapping("{id}/join-with-code/{code}")
    override fun joinWithCodeEndpoint(@PathVariable id: Int, @PathVariable code: String): ResponseEntity<Reader?> {
        val result = textService.joinWithCode(id, user!!, code)

        return when (result.result) {
            TextService.JoinWithCodeResult.OK -> ok(result.reader)
            else -> badRequest()
        }
    }

    @GetMapping("{id}")
    override fun textSetByIdEndpoint(@PathVariable id: Int): ResponseEntity<TextSet> = of(textService.getTextSet(id, user!!))

    @GetMapping("{id}/readers")
    override fun readersByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Reader>> = of(textService.getTextSetReaders(id, user!!))

    @GetMapping("{id}/texts/all")
    override fun textsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>> = of(textService.getAllTexts(id, user!!))

    @GetMapping("{id}/texts/all/visible")
    override fun allVisibleTextsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>> = of(textService.getAllVisibleTexts(id, user!!))

    @GetMapping("{id}/texts/past")
    override fun pastTextsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>> = of(textService.getPastTexts(id, user!!))

    class AddTextPayload {
        lateinit var text: String

        @JsonFormat(pattern = "yyyy-MM-dd")
        var date: LocalDate? = null
        var order: Int = 0
    }

    @PutMapping("{id}/add")
    override fun addTextByIdEndpoint(@PathVariable id: Int, @RequestBody payload: AddTextPayload): ResponseEntity<Text> =
            of(textService.addText(id, user!!, payload.text, payload.date, payload.order))

    class CreateNewTextSetPayload {
        lateinit var title: String
        lateinit var description: String
    }

    @PostMapping("/create-new")
    override fun createNewTextSetEndpoint(@RequestBody payload: CreateNewTextSetPayload): ResponseEntity<TextSet> =
            of(textService.createNewTextSet(user!!, payload.title, payload.description))

    @PostMapping("{id}/create-join-link")
    override fun joinLinkEndpoint(@PathVariable id: Int): ResponseEntity<JoinLink> = of(joinLinkService.createJoinLink(id, user!!))

    @DeleteMapping("{setId}/{textId}")
    override fun deleteTextByIdEndpoint(@PathVariable setId: Int, @PathVariable textId: Int): ResponseEntity<Boolean> =
            ofBoolean(textService.deleteText(setId, textId, user!!))

    @GetMapping("where-i-am-owner")
    override fun setsIAmOwnerEndpoint(): ResponseEntity<List<TextSet>> = of(textService.getAllByOwner(user!!))

    @GetMapping("where-i-am-reader")
    override fun setsIAmReaderEndpoint(): ResponseEntity<List<TextSet>> = of(textService.getAllByReader(user!!))

    @GetMapping("summary")
    override fun summaryEndpoint(): ResponseEntity<List<SummaryService.SummaryObject>> = ok(summaryService.createSummaryFor(user!!))

    @GetMapping("{setId}/readers/include-users")
    override fun readersIncludeUsersEndpoint(@PathVariable setId: Int): ResponseEntity<Iterable<TextService.ReaderIncludeUser>> = of(textService.getReadersIncludeUsers(user!!, setId))

    @DeleteMapping("{setId}")
    override fun deleteTextSetByIdEndpoint(@PathVariable setId: Int): ResponseEntity<Boolean> = ofBoolean(textService.deleteTextSet(user!!, setId))

    @GetMapping("{setId}/join-links")
    override fun joinLinksEndpoint(@PathVariable setId: Int): ResponseEntity<List<JoinLink>> = of(joinLinkService.getJoinLinks(setId, user!!))

    @DeleteMapping("{setId}/quit")
    override fun quitTextSetByIdEndpoint(@PathVariable setId: Int): ResponseEntity<Boolean> = ofBoolean(textService.quit(user!!, setId))

    @GetMapping("{setId}/{textId}")
    override fun textByIdEndpoint(@PathVariable textId: Int, @PathVariable setId: Int): ResponseEntity<Text> = of(textService.getTextByIdAndSetId(user!!, textId, setId))

    @DeleteMapping("{setId}/join-link/{joinLinkId}")
    override fun deleteJoinLinkByIdEndpoint(@PathVariable setId: Int, @PathVariable joinLinkId: Int): ResponseEntity<Boolean> = ofBoolean(joinLinkService.deleteJoinLink(user!!, setId, joinLinkId))


    @DeleteMapping("{setId}/readers/{readerId}")
    override fun deleteReaderByIdEndpoint(@PathVariable setId: Int, @PathVariable readerId: Int): ResponseEntity<Boolean> = ofBoolean(textService.deleteReader(user!!, setId, readerId))

    @GetMapping("/text/by-id/{textId}")
    override fun textByJustIdEndpoint(@PathVariable textId: Int): ResponseEntity<Text> = of(textService.getTextById(user!!, textId))

    @PostMapping("join-with-code/{code}")
    override fun joinWithCode(@PathVariable code: String): ResponseEntity<Reader?> = of(textService.joinWithCode(user!!, code))
}