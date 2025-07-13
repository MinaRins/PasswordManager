package models

data class User(
    var userId: Int,
    var name: String,
    var email: String,
    var masterPassword: String
)
