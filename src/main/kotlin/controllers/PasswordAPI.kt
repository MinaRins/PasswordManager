package controllers

    import models.Password
    import persistence.Serializer

    class PasswordAPI(serializerType: Serializer) {

        private var serializer: Serializer = serializerType
        private var passwords = ArrayList<Password>()
        fun add(newPassword: Password): Boolean {
            return passwords.add(newPassword)
        }

        fun listAllPasswords(): String =
            if (passwords.isEmpty()) {
                "No password stored"
            } else {
                formatListString(passwords)
            }

        fun deletePassword(IDToDelete: Int): Password? {
            return if (isValidListID(IDToDelete)) {
                passwords.removeAt(IDToDelete)
            } else {
                null
            }
        }

        fun updatePassword(IDToUpdate: Int, password: Password?): Boolean {
            val foundPassword = findPassword(IDToUpdate)

            if ((foundPassword != null) && (password != null)) {
                foundPassword.Username = password.Username
                foundPassword.App = password.App
                foundPassword.Password = password.Password
                foundPassword.PasswordID = password.PasswordID
                return true
            }
            return false
        }

        fun searchByApp(searchString: String) =
            formatListString(
                passwords.filter { password -> password.Username.contains(searchString, ignoreCase = true) }
            )

        fun isValidListID(ID: Int): Boolean {
            return isValidListID(ID)
        }

         fun findPassword(ID: Int): Password? {
         return if (isValidListID(ID)) {
         passwords[ID]
         } else {
          null
       }
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
