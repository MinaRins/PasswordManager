package controllers

    import models.Password
    import persistence.Serializer

    class PasswordAPI(serializerType: Serializer) {

        private var serializer: Serializer = serializerType
        private var passwords = ArrayList<Password>()
        fun add(newPassword: Password): Boolean {
            return passwords.add(newPassword)
        }

        fun listAllPasswords(): List<Password> = passwords

        fun deletePassword(IDToDelete: Int): Password? {
            return if (isValidListID(IDToDelete)) {
                passwords.removeAt(IDToDelete)
            } else {
                null
            }
        }

        fun updatePassword(IDToUpdate: Int, updatedPassword: Password): Boolean {
            val foundPassword = findPassword(IDToUpdate)

            if (foundPassword != null) {
                foundPassword.Username = updatedPassword.Username
                foundPassword.App = updatedPassword.App
                foundPassword.Password = updatedPassword.Password
                // Do not update the ID!
                return true
            }
            return false
        }

        fun findOne(App: String?): Password? {
            return passwords.find { password -> password.App.equals(App, ignoreCase = true) }
        }

        fun findPassword(ID: Int):
                Password? {
            return if (isValidListID(ID)) {
                passwords[ID]
            } else {
                null
            }
        }

        fun isValidListID(index: Int): Boolean {
            return index >= 0 && index < passwords.size
        }

        fun numberOfPasswords(): Int {
            return passwords.size
        }


        @Throws(Exception::class)
        fun load() {
            passwords = serializer.read() as ArrayList<Password>
        }

        @Throws(Exception::class)
        fun store() {
            serializer.write(passwords)
        }

        private fun formatListString(passwordsToFormat: List<Password>): String =
            passwordsToFormat
                .joinToString(separator = "\n") { note ->
                    passwords.indexOf(note).toString() + ": " + note.toString()
                }
    }
