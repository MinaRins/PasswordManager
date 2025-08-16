package controllers

import models.Password
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import persistence.XMLSerializer
import java.io.File

class PasswordAPITest {

    //variables for testing
    //sample password
    private var samplePassword: Password? = null
    //passwordapi instance
    private var populatedPasswords: PasswordAPI? = null

    //method 2 initialise test data b4 the test
    @BeforeEach
    fun setup() {
        //creates sample password obj
        samplePassword = Password("user1", "App1", "pass1", 1)
        //initialises passwordapi to the xml file
        populatedPasswords = PasswordAPI(XMLSerializer(File("passwords.xml")))
        //adds the sample password to the passwordapi 2 test
        populatedPasswords!!.add(samplePassword!!)  // Initialize properly
    }

    //method cleans up the data after the test
    @AfterEach
    fun tearDown() {
        samplePassword = null
        populatedPasswords = null
    }


    //test to check when adding a password increases the size of password list
    @Test
    fun `adding a password increases the list size`() {
        //creates password object
        val newPassword = Password("user2", "App2", "pass2", 2)
        //verifies if initial size of password list is 1
        assertEquals(1, populatedPasswords!!.numberOfPasswords())
        //add the new password and makes sure the add method returns true
        assertTrue(populatedPasswords!!.add(newPassword))
        //verifies size of password list has increased to 2
        assertEquals(2, populatedPasswords!!.numberOfPasswords())
    }

    //test to retrieve a password by valid index
    @Test
    fun `retrieving a password by valid index returns the correct password`() {
        val retrievedPassword = populatedPasswords!!.get(0)
        assertEquals(samplePassword, retrievedPassword)
    }

    //test to retrieve a password by invalid index
    @Test
    fun `retrieving a password by invalid index returns null`() {
        val retrievedPassword = populatedPasswords!!.get(100)  // index out of bounds
        assertNull(retrievedPassword)
    }

    //test to delete a password and verify size decreases
    @Test
    fun `deleting a password reduces the list size`() {
        assertEquals(1, populatedPasswords!!.numberOfPasswords())
        val deleted = populatedPasswords!!.delete(0)
        assertTrue(deleted)
        assertEquals(0, populatedPasswords!!.numberOfPasswords())
    }

    //test deleting a password with invalid index
    @Test
    fun `deleting with invalid index returns false`() {
        val deleted = populatedPasswords!!.delete(99)
        assertFalse(deleted)
    }

    //test if list is empty initially (after deleting all)
    @Test
    fun `list is empty after deleting all passwords`() {
        populatedPasswords!!.delete(0)
        assertTrue(populatedPasswords!!.isEmpty())
    }
}