package pl.codziennemotto.controllers.textset

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import pl.codziennemotto.data.dto.JoinLink
import pl.codziennemotto.data.dto.Reader
import pl.codziennemotto.data.dto.Text
import pl.codziennemotto.data.dto.TextSet
import pl.codziennemotto.services.summary.SummaryService
import pl.codziennemotto.services.text.TextService

@Tag(name = "TextSet", description = "Work with Text/TextSet")
interface TextSetApi {
    @Operation(
            operationId = "TextSetController#joinWithCodeEndpoint",
            description = "Join TextSet using join link.",
            responses = [
                ApiResponse(responseCode = "200", description = "OK - Reader created."),
                ApiResponse(responseCode = "403", description = "Forbidden - No valid authentication."),
                ApiResponse(responseCode = "400", description = "Bad request - probably there's no code in database.")
            ]
    )
    fun joinWithCodeEndpoint(@PathVariable id: Int, @PathVariable code: String): ResponseEntity<Reader?>

    @Operation(
            operationId = "TextSetController#textSetByIdEndpoint",
            description = "Get TextSet by ID",
            responses = [
                ApiResponse(responseCode = "204", description = "Deleted"),
                ApiResponse(responseCode = "403", description = "Forbidden - No valid authentication."),
                ApiResponse(responseCode = "400", description = "you're not owner of target TextSet, or TextSet doesn't exist.")
            ]
    )
    fun textSetByIdEndpoint(@PathVariable id: Int): ResponseEntity<TextSet>

    @Operation(
            operationId = "TextSetController#readersByIdEndpoint",
            description = "Get TextSet's Readers",
            responses = [
                ApiResponse(responseCode = "200", description = "OK"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - You're not owner of the TextSet or it doesn't exist.")
            ]
    )
    fun readersByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Reader>>

    @Operation(
            operationId = "TextSetController#textSetByIdEndpoint",
            description = "Get all TextSet`s text. Only for TextSet's owner",
            responses = [
                ApiResponse(responseCode = "200", description = "OK"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - You're not owner of the TextSet or it doesn't exist.")
            ]
    )
    fun textsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>>

    @Operation(
            operationId = "TextSetController#allVisibleTextsByIdEndpoint",
            description = "Get all TextSet`s text where authorized user is allowed to see.",
            responses = [
                ApiResponse(responseCode = "200", description = "OK"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - You're not allowed to access the TextSet.")
            ]
    )
    fun allVisibleTextsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>>

    @Operation(
            operationId = "TextSetController#pastTextsByIdEndpoint",
            description = "Get past TextSet`s text.",
            responses = [
                ApiResponse(responseCode = "200", description = "OK"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - You're not reader or owner of the TextSet or it doesn't exist.")
            ]
    )
    fun pastTextsByIdEndpoint(@PathVariable id: Int): ResponseEntity<List<Text>>

    @Operation(
            operationId = "TextSetController#addTextByIdEndpoint",
            description = "Add text to TextSet",
            responses = [
                ApiResponse(responseCode = "200", description = "Successfully created"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - You're not owner of the TextSet or it doesn't exist")
            ]
    )
    fun addTextByIdEndpoint(@PathVariable id: Int, @RequestBody payload: TextSetController.AddTextPayload): ResponseEntity<Text>

    @Operation(
            operationId = "TextSetController#createNewTextSetEndpoint",
            description = "Create new TextSet",
            responses = [
                ApiResponse(responseCode = "200", description = "TextSet successfully created"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication")
            ]
    )
    fun createNewTextSetEndpoint(@RequestBody payload: TextSetController.CreateNewTextSetPayload): ResponseEntity<TextSet>

    fun joinLinkEndpoint(@PathVariable id: Int): ResponseEntity<JoinLink>

    @Operation(
            operationId = "TextSetController#deleteTextByIdEndpoint",
            description = "Delete text",
            responses = [
                ApiResponse(responseCode = "204", description = "Successfully deleted."),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "You're not owner of the TextSet or it doesn't exist.")
            ]
    )
    fun deleteTextByIdEndpoint(@PathVariable setId: Int, @PathVariable textId: Int): ResponseEntity<Boolean>

    @Operation(
            operationId = "TextSetController#setsIAmOwnerEndpoint",
            description = "Returns list of TextSet where authenticated user is owner",
            responses = [
                ApiResponse(responseCode = "204", description = "Successfully obtained."),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication")
            ]
    )
    fun setsIAmOwnerEndpoint(): ResponseEntity<List<TextSet>>

    @Operation(
            operationId = "TextSetController#setsIAmReaderEndpoint",
            description = "Returns list of TextSet where authenticated user is reader",
            responses = [
                ApiResponse(responseCode = "204", description = "Successfully obtained."),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication")
            ]
    )
    fun setsIAmReaderEndpoint(): ResponseEntity<List<TextSet>>

    @Operation(
            operationId = "TextSetController#summaryEndpoint",
            description = "Returns summary of all list's that authenticated user has access",
            responses = [
                ApiResponse(responseCode = "200", description = "Successfully obtained"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication")
            ]
    )
    fun summaryEndpoint(): ResponseEntity<List<SummaryService.SummaryObject>>

    @Operation(
            operationId = "TextSetController#readersIncludeUsersEndpoint",
            responses = [
                ApiResponse(responseCode = "200", description = "Successfully fetch"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - no matching data corresponding to your authentication.")
            ]
    )
    fun readersIncludeUsersEndpoint(@PathVariable setId: Int): ResponseEntity<Iterable<TextService.ReaderIncludeUser>>

    @Operation(
            operationId = "TextSetController#deleteTextSetByIdEndpoint",
            description = "Delete TextSet",
            responses = [
                ApiResponse(responseCode = "204", description = "Deleted"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - you're not owner of target TextSet, or TextSet doesn't exist.")
            ]
    )
    fun deleteTextSetByIdEndpoint(@PathVariable setId: Int): ResponseEntity<Boolean>

    @Operation(
            operationId = "TextSetController#joinLinksEndpoint",
            description = "Gets all JoinLink's in TextSet [even expired]",
            responses = [
                ApiResponse(responseCode = "200", description = "Ok"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - You're not owner of the TextSet or it doesn't exist.")
            ]
    )
    fun joinLinksEndpoint(@PathVariable setId: Int): ResponseEntity<List<JoinLink>>

    @Operation(
            operationId = "TextSetController#quitTextSetByIdEndpoint",
            description = "Reader leaves the TextSet.",
            responses = [
                ApiResponse(responseCode = "204", description = "Ok"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "bad request - you're not a reader of the TextSet")
            ]
    )
    fun quitTextSetByIdEndpoint(@PathVariable setId: Int): ResponseEntity<Boolean>

    @Operation(
            operationId = "TextSetController#textByIdEndpoint",
            responses = [
                ApiResponse(responseCode = "200", description = "OK"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "Something wrong, target Text must be accessible for you [If you're reader, Text must be past]")
            ]
    )
    fun textByIdEndpoint(@PathVariable textId: Int, @PathVariable setId: Int): ResponseEntity<Text>

    @Operation(
            operationId = "TextSetController#deleteJoinLinkByIdEndpoint",
            description = "Delete JoinLink",
            responses = [
                ApiResponse(responseCode = "204", description = "Deleted JoinLink"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "Bad Request - You're not owner of TextSet's, or JoinLink is not exist.")
            ]
    )
    fun deleteJoinLinkByIdEndpoint(@PathVariable setId: Int, @PathVariable joinLinkId: Int): ResponseEntity<Boolean>

    @Operation(
            operationId = "TextSetController#deleteReaderByIdEndpoint",
            description = "Deletes Reader",
            responses = [
                ApiResponse(responseCode = "204", description = "Deleted"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "Bad Request - You're not owner of TextSet's, or JoinLink is not exist.")
            ]
    )
    fun deleteReaderByIdEndpoint(@PathVariable setId: Int, @PathVariable readerId: Int): ResponseEntity<Boolean>

    @Operation(
            operationId = "TextSetController#textByJustIdEndpoint",
            description = "Get Text by ID",
            responses = [
                ApiResponse(responseCode = "200", description = "Successfully fetched"),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "You're not allowed to see this Text.")
            ]
    )
    fun textByJustIdEndpoint(@PathVariable textId: Int): ResponseEntity<Text>

    @Operation(
            operationId = "TextSetController#joinWithCode",
            description = "Join TextSet with code",
            responses = [
                ApiResponse(responseCode = "200", description = "OK - Reader created."),
                ApiResponse(responseCode = "403", description = "Forbidden - no valid authentication"),
                ApiResponse(responseCode = "400", description = "Bad request - probably there's no code in database.")
            ]
    )
    fun joinWithCode(@PathVariable code: String): ResponseEntity<Reader?>
}