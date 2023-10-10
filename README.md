# CodzienneMotto.pl

API Server, made in **Spring** in ❤ Kotlin 😎😋

**CodzienneMotto.pl** allows you to create a lists of daily quotes, invite users to join your list and schedule quote to specific day
<br>
See [cross-platform application](https://www.github.com/kacperfaber/codziennemotto-pl-app)

[![Integration Tests](https://github.com/kacperfaber/codziennemotto_pl_server/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/kacperfaber/codziennemotto_pl_server/actions/workflows/integration-tests.yml)
[![Web Tests](https://github.com/kacperfaber/codziennemotto_pl_server/actions/workflows/integration-tests.yml/badge.svg)](https://github.com/kacperfaber/codziennemotto_pl_server/actions/workflows/web-tests.yml)

<img src="assets/banner.png"/>

## Install 

If you want to run my server locally

#### Download last version from github
```shell
git clone https://www.github.com/kacperfaber/codziennemotto_pl_server && cd codziennemotto_pl_server
```

#### Run developer profile locally using gradle

```shell
# Run locally and setup testing database
runDev

# Run locally, but without testing database.
runDevNoTestingDb
```

## Configuration

Configuration file it's generally a .properties file. File name is dynamic and it depends on current profile.

```properties
# ./application.{profile}.properties

# Email Client Settings #
# ===================== #

# mailer.from
# mailer.username
# mailer.password
# mailer.smtp
# mailer.port

# Verification Email Settings #
# =========================== #

# verification_email.subject: Subject of the verification email

# MySQL Database #
# ============== #

# db.url: Must contain database name.
# db.username
# #db.password

# Security #
# ======== #

# cors.allow-origin: Access-Control-Allow-Origin header with this value will be added to each response.
# token_hasher.secret: Will encode/decode access tokens using this secret (using AES algorithm)
# password_encoder.secret: Will encode and compare user passwords using PBKDF2 algorithm

# Optional #
# ======== #

# testing.initdb (bool): Only when profile is not production, if it's production won't be called.
#   Will generate random database for testing.
```

## See API docs
Swagger UI is here
```http request
/swagger-ui/index.html
```

## Author
Kacper Faber
