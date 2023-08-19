package pl.codziennemotto.controllers.register

data class ConfirmPayload(val emailAddress: String, val code: String)