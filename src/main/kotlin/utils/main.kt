
import controllers.PasswordAPI
import models.Password
import persistence.JSONSerializer
import utils.readNextInt
import utils.readNextLine
import java.io.File
import kotlin.system.exitProcess

// private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
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
            2 -> listPassword()
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

fun listPassword() {
    if (PasswordAPI.numberOfPasswords() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL Passwords         |
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
    fun exitApp() {
        println("Exiting...bye")
        exitProcess(0)
    }
}

fun save() {
    try {
        PasswordAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun searchPassword() {
    val searchApp = readNextLine("Enter the App or Website")
    val searchResults = PasswordAPI.searchByApp(searchApp)
    if (searchResults.isEmpty()) {
        println("No Passwords found")
    } else {
        (searchResults)
    }
}

fun deletePassword() {
    PasswordAPI.listAllPasswords()
    if (PasswordAPI.numberOfPasswords() > 0) {
        val IDToDelete = readNextInt("Enter the ID of the Password to delete: ")
        val PasswordToDelete = PasswordAPI.deletePassword(IDToDelete)
        if (PasswordToDelete != null) {
            println("Delete Successful! Deleted Password: ${PasswordToDelete.Username}")
        } else {
            println("Delete is not Successful ):")
        }
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

fun load() {
    try {
        PasswordAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

    fun updatePassword() {
        PasswordAPI.listAllPasswords()
        if (PasswordAPI.numberOfPasswords() > 0) {
            val IDToUpdate = readNextInt("Enter the ID of the Password to update: ")
            if (PasswordAPI.isValidListID(IDToUpdate)) {
                val Username = readLine().toString()
                print("Enter the app/website for the password: ")
                val App = readLine().toString()
                print("Enter a Password: ")
                val Password = readLine().toString()
                print("Enter a Password ID: ")
                val PasswordID = readLine()?.toInt()

                if (PasswordAPI.updatePassword(IDToUpdate, Password(Username, App, Password, PasswordID))) {
                    println("Update has been made")
                } else {
                    println("Update Failed")
                }
            }
        }
    }
