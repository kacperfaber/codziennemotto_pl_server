package pl.codziennemotto.services.user.registration

enum class RegisterResult(val value: String) {
    EmailOrUsernameTaken("email_or_username_taken"),
    Ok("ok")
}