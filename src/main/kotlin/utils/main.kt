import controllers.PasswordAPI
import models.Password
import controllers.UserAPI
import models.User
import persistence.JSONSerializer
import utils.readNextInt
import java.io.File
import kotlin.system.exitProcess

//makes private val passwordAPI = PasswordAPI(XMLSerializer(File("passwords.xml")))
private val PasswordAPI = PasswordAPI(JSONSerializer(File("passwords.json")))
private val userAPI = UserAPI()
private var loggedInUser: User? = null


fun loginMenu(): Int {
    println(
        """
        > ----------------------------
        > ☆     Login or Register    ☆
        > ----------------------------
        > 1) Login
        > 2) Register
        > 3) Exit
        """.trimMargin(">")
    )
    return readNextInt(" > ==>> ")
}

fun main() {
    runMenu()
}

fun mainMenu(): Int {
    print(
        """ 
         > ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         > ☆        Password Manager        ☆
         > ⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸⧸
         > ☆ PASSWORD MENU                  ☆
         > ☆   1) Add a password            ☆
         > ☆   2) List all passwords        ☆
         > ☆   3) Update a password         ☆
         > ☆   4) Delete a password         ☆        
         > ☆   5) Search password (by app)  ☆
         > ☆   6) Save password             ☆
         > ☆   7) Load password             ☆
         > ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         > ☆   8) Exit                      ☆
         > ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         >""".trimMargin(">")
    )
    //reads users menuchoice
    return readNextInt(" > ==>>")
}


 //function 2 handle menu and call the right actions based on the users choice
fun runMenu() {
    //Show login/register menu until user logs in or exits
    while (loggedInUser == null) {
        when (loginMenu()) {
            1 -> loginUser()
            2 -> registerUser()
            3 -> exitApp()
            else -> println("Invalid option entered.")
        }
    }

    //user is logged in, password manager menu
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
            else -> println("Invalid # entered v_v: $option")
        }
    } while (true)
}

fun loginUser() {
    println("== Login ==")
    print("Enter your email: ")
    val email = readLine().orEmpty()
    print("Enter your master password: ")
    val password = readLine().orEmpty()

    val user = userAPI.login(email, password)
    if (user != null) {
        loggedInUser = user
        println("Login successful. Welcome, ${user.name}!")
    } else {
        println("Login failed. Please try again.")
    }
}

fun registerUser() {
    println("== Register a New User ==")
    print("Enter your name: ")
    val name = readLine().orEmpty()
    print("Enter your email: ")
    val email = readLine().orEmpty()
    print("Enter your master password: ")
    val masterPassword = readLine().orEmpty()

    val userId = userAPI.numberOfUsers()
    val newUser = User(userId, name, email, masterPassword)

    if (userAPI.add(newUser)) {
        println("Registration successful. Please login now.")
    } else {
        println("Registration failed.")
    }
}

fun load() {
    try {
        PasswordAPI.load()
    } catch (e: Exception) {
        //handles any loading errors
        System.err.println("Error reading from file: $e")
    }
}

fun deletePassword() {
    //checks if there are any passwords to delete
    if (PasswordAPI.numberOfPasswords() > 0) {
        //lists all the paswords
        val passwords = PasswordAPI.listAllPasswords()
        println("Here are all your passwords!")
        passwords.forEachIndexed { index, password ->
            println("$index) ${password.Username} - ${password.App} - ${password.PasswordID}")
        }


         //asks for the id of the password to delete

        val IDToDelete = readNextInt("Enter the ID of the Password to delete: ")
        //tries 2 delete
        val passwordToDelete = PasswordAPI.deletePassword(IDToDelete)
        //if delete is successful
        if (passwordToDelete != null) {
            println("Delete Successful! Deleted Password: ${passwordToDelete.Username}")
        } else {
            println("Delete isnt successful. ]: ")
        }
    } else {
        println("No passwords to delete! :o")
    }
}
fun updatePassword() {

     //checks if there are any passwords to delete

    if (PasswordAPI.numberOfPasswords() > 0) {
        //list all passwords
        val passwords = PasswordAPI.listAllPasswords()
        println("Here are all of ur Passwords!")
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

    //tries 2 add the password
    val isAdded = PasswordAPI.add(Password(Username, App, Password, PasswordID))
//if successful
    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listPassword(passwordAPI: PasswordAPI) {

     //gets list of passwords

    val passwords = passwordAPI.listAllPasswords()
    if (passwords.isEmpty()) {
        println("No passwords available.")
        return
    } else {
        println("Here are all the available passwords:")
        //uses teh withIndex() to get index and object
        for ((index, password) in passwords.withIndex()) {
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
        println("option invalid, theres No password stored")
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
        //handles any saving errors
        System.err.println("error writing to file: $e")
    }
}

fun getPasswordByApp(): Password? {
    print("Enter the App/Website to search by: ")
    val App = readLine()
    return PasswordAPI.findOne(App)
}

fun searchPassword() {
    println("Searching for password")
    //gets password by app or the website name
    val searchedPassword = getPasswordByApp()
    if (searchedPassword == null) {
        println("No password found :c")
    } else {
        println("Password found! :D $searchedPassword")
    }
}
