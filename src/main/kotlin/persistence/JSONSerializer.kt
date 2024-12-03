package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import models.Password
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class JSONSerializer(private val file: File) : Serializer {

    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(JettisonMappedXmlDriver())
        // Allow only the Password class for security reasons
        xStream.allowTypes(arrayOf(Password::class.java))

        // Deserialize the object from the file
        FileReader(file).use { reader ->
            val inputStream = xStream.createObjectInputStream(reader)
            val obj = inputStream.readObject()
            inputStream.close()
            return obj
        }
    }

    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(JettisonMappedXmlDriver())

        // Serialize the object to the file
        FileWriter(file).use { writer ->
            val outputStream = xStream.createObjectOutputStream(writer)
            outputStream.writeObject(obj)
            outputStream.close()
        }
    }
}