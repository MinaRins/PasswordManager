
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

fun listPasswords() {
    if (PasswordAPI.numberOfPasswords() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL Passwords         |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllPasswords()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No Password stored")
    }
}

fun addPassword() {
    logger.info { "addPassword() function invoked" }
    print("Please a Username for your Password: ")
    val Username = readLine().toString()
    print("Enter the app/website for the password: ")
    val App = readLine()?.toInt()
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
fun updateNote() {
    // logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        // only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readValidPriority("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readValidCategory("Enter a category for the note from $categories: ")

            // pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun deleteNote() {
    // logger.info { "deleteNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        // only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        // pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun searchNotes() {
    val searchTitle = readNextLine("Enter the description to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No notes found")
    } else {
        println(searchResults)
    }
}

fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}

fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listArchivedNotes() {
    println(noteAPI.listArchivedNotes())
}

fun archiveNote() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        // only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        // pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}