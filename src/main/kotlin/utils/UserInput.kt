package utils

//reads line from console and converts it into an int, if input is not a number returns -1
fun readIntNotNull() = readlnOrNull()?.toIntOrNull() ?: -1

//prints prompt to user and keeps looping until a valid int is entered
fun readNextInt(prompt: String?): Int {
    do {
        try {
            print(prompt)
            return readln().toInt()
        } catch (e: NumberFormatException) {
            System.err.println("Enter a number please.")
        }
    } while (true)
}

//ensures valid decimal number
fun readNextDouble(prompt: String?): Double {
    do {
        try {
            print(prompt)
            return readln().toDouble()
        } catch (e: NumberFormatException) {
            System.err.println("Enter a number please.")
        }
    } while (true)
}

//ensures valid float
fun readNextFloat(prompt: String?): Float {
    do {
        try {
            print(prompt)
            return readln().toFloat()
        } catch (e: NumberFormatException) {
            System.err.println("Enter a number please.")
        }
    } while (true)
}

//prints prompt and returns next line as string
fun readNextLine(prompt: String?): String {
    print(prompt)
    return readln()
}

//prints prompt, reads input, if not empty returns first character and keeps looping until character is entered
fun readNextChar(prompt: String?): Char {
    while (true) {
        try {
            print(prompt)
            val input = readln()
            if (input.isNotEmpty()) {
                return input.first()
            } else {
                System.err.println("Enter a character please.")
            }
        } catch (e: Exception) {
            //description of what went wrong
            System.err.println("An error occurred: ${e.message}")
        }
    }
}