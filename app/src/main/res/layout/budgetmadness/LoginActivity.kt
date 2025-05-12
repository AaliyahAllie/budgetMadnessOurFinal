package com.example.budgetmadness

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d(TAG, "LoginActivity started")
        logToFile("LoginActivity started")

        dbHelper = DatabaseHelper(this)

        val username = findViewById<EditText>(R.id.loginUsername)
        val password = findViewById<EditText>(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.loginBtnFinal)

        loginBtn.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            val logAttempt = "Login attempt - Username: $user"
            Log.d(TAG, logAttempt)
            logToFile(logAttempt)

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                if (dbHelper.validateUser(user, pass)) {
                    val successMsg = "Login successful for user: $user"
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, successMsg)
                    logToFile(successMsg)

                    startActivity(Intent(this, StarterPageActivity::class.java))
                    finish()
                } else {
                    val failMsg = "Login failed - Invalid credentials for user: $user"
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, failMsg)
                    logToFile(failMsg)
                }
            } else {
                val emptyFieldsMsg = "Login failed - Missing username or password"
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                Log.w(TAG, emptyFieldsMsg)
                logToFile(emptyFieldsMsg)
            }
        }
    }

    private fun logToFile(message: String) {
        try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val logFile = File(filesDir, "app_log.txt")
            logFile.appendText("[$timestamp] $message\n")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write to log file", e)
        }
    }
}
