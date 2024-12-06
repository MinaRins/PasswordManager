import controllers.PasswordAPI
import models.Password
import persistence.JSONSerializer
import utils.readNextInt
import utils.readNextLine
import java.io.File
import kotlin.system.exitProcess

// private val passwordAPI = PasswordAPI(XMLSerializer(File("passwords.xml")))
private val PasswordAPI = PasswordAPI(JSONSerializer(File("notes.json")))

fun main() {
    runMenu()
}

fun mainMenu(): Int {
    print(
        """ 
         > ----------------------------------
         > |        Password Manager        |
         > | PASSWORD MENU                  |
         > |   1) Add a password            |
         > |   2) List all passwords        |
         > |   3) Update a password         |
         > |   4) Delete a password         |         
         > |   5) Search password (by app)  |
         > |   6) Save password             |
         > |   7) Load password             |
         > ----------------------------------
         > |   8) Exit                      |
         > ---------------------------------- 
         >""".trimMargin(">")
    )
    return readNextInt(" > ==>>")
}

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addPassword()
            2 -> listPassword(PasswordAPI)
            3 -> updatePassword()
            4 -> deletePassword()
            5 -> searchPassword()
            6 -> save()
            7 -> load()
            8 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

fun load() {
    try {
        PasswordAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun deletePassword() {
    if (PasswordAPI.numberOfPasswords() > 0) {
        val passwords = PasswordAPI.listAllPasswords()
        println("Here are all your passwords!")
        passwords.forEachIndexed { index, password ->
            println("$index) ${password.Username} - ${password.App} - ${password.PasswordID}")
        }
        //ID of the password to delete
        val IDToDelete = readNextInt("Enter the ID of the Password to delete: ")
        val passwordToDelete = PasswordAPI.deletePassword(IDToDelete)

        if (passwordToDelete != null) {
            println("Delete Successful! Deleted Password: ${passwordToDelete.Username}")
        } else {
            println("Delete isnt successful")
        }
    } else {
        println("No passwords to delete! :o")
    }
}
fun updatePassword() {
    if (PasswordAPI.numberOfPasswords() > 0) {
        // List all passwords
        val passwords = PasswordAPI.listAllPasswords()
        println("Here are all your passwords:")
        passwords.forEach {
            println("ID: ${it.PasswordID}, Username: ${it.Username}, App: ${it.App}")
        }
        val IDToUpdate = readNextInt("Enter the ID of the Password to update: ")

        if (PasswordAPI.isValidListID(IDToUpdate)) {
            print("Enter the updated Username: ")
            val Username = readLine().orEmpty()
            print("Enter the updated App/Website: ")
            val App = readLine().orEmpty()
            print("Enter the updated Password: ")
            val Password = readLine().orEmpty()
            if (PasswordAPI.updatePassword(IDToUpdate, Password(Username, App, Password, IDToUpdate))) {
                println("Update has been made successfully!")
            } else {
                println("Update Failed. Could not find a password with the given ID.")
            }
        } else {
            println("Invalid Password ID.")
        }
    } else {
        println("No passwords available to update.")
    }
}

fun addPassword() {
    print("Please a Username for your Password: ")
    val Username = readLine().toString()
    print("Enter the app/website for the password: ")
    val App = readLine().toString()
    print("Enter a Password: ")
    val Password = readLine().toString()
    print("Enter a Password ID: ")
    val PasswordID = readLine()?.toInt()

    val isAdded = PasswordAPI.add(Password(Username, App, Password, PasswordID))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listPassword(passwordAPI: PasswordAPI) {
        val passwords = passwordAPI.listAllPasswords() // Retrieve the list of passwords
        if (passwords.isEmpty()) {
            println("No passwords available.")
            return
        } else {
            println("Here are all the available passwords:")
            for ((index, password) in passwords.withIndex()) { // Use withIndex() to get index and object
                println("${index + 1}. ${password.Username} - ${password.App} - ${password.Password} (${password.PasswordID})")
            }
        }


    if (PasswordAPI.numberOfPasswords() > 0) {

        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View Password Manager    |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> PasswordAPI.listAllPasswords()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No Password stored")
    }
}

fun exitApp() {
        println("Exiting...bye")
        exitProcess(0)
    }

fun save() {
    try {
        PasswordAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun getPasswordByApp(): Password? {
    print("Enter the App/Website to search by: ")
    val App = readLine()
    return PasswordAPI.findOne(App)
}

fun searchPassword() {
    println("Searching for password")

    val searchedPassword = getPasswordByApp()
    if (searchedPassword == null) {
        println("No passowrd found :c")
    } else {
        println("Password found! :D $searchedPassword")
    }
}