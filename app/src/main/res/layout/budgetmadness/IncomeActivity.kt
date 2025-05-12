package com.example.budgetmadness

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class IncomeActivity : AppCompatActivity() {

    private lateinit var editCashIncome: EditText
    private lateinit var editCardIncome: EditText
    private lateinit var btnSave: Button
    private lateinit var dbHelper: IncomeDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        editCashIncome = findViewById(R.id.edit_cash_income)
        editCardIncome = findViewById(R.id.edit_card_income)
        btnSave = findViewById(R.id.btn_save)

        dbHelper = IncomeDatabaseHelper(this)

        btnSave.setOnClickListener {
            val cashStr = editCashIncome.text.toString()
            val cardStr = editCardIncome.text.toString()

            if (cashStr.isBlank() || cardStr.isBlank()) {
                Toast.makeText(this, "Please enter both values", Toast.LENGTH_SHORT).show()
                logEvent("User tried to save income but one or both fields were empty.")
                return@setOnClickListener
            }

            try {
                val cash = cashStr.toDouble()
                val card = cardStr.toDouble()
                dbHelper.insertIncome(cash, card)
                Toast.makeText(this, "Income saved successfully!", Toast.LENGTH_SHORT).show()
                logEvent("Income saved successfully: Cash = $cash, Card = $card")
                editCashIncome.text.clear()
                editCardIncome.text.clear()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show()
                logEvent("Failed to save income: Invalid number format for Cash = $cashStr or Card = $cardStr")
            }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_income -> {
                    logEvent("Navigating to IncomeActivity")
                    startActivity(Intent(this, IncomeActivity::class.java))
                    true
                }
                R.id.nav_home -> {
                    logEvent("Navigating to StarterPageActivity")
                    startActivity(Intent(this, StarterPageActivity::class.java))
                    true
                }
                R.id.nav_add -> {
                    logEvent("Navigating to AddExpensesActivity")
                    startActivity(Intent(this, AddExpensesActivity::class.java))
                    true
                }
                R.id.nav_open_menu -> {
                    logEvent("Navigating to MenuActivity")
                    startActivity(Intent(this, MenuActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun logEvent(message: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val logMessage = "$timestamp - $message"
        android.util.Log.d("IncomeActivityLog", logMessage)

        try {
            val file = File(getExternalFilesDir(null), "app_log.txt")
            val writer = FileWriter(file, true)
            writer.appendLine(logMessage)
            writer.close()
        } catch (e: IOException) {
            android.util.Log.e("IncomeActivityLog", "Failed to write log: ${e.message}")
        }
    }
}
