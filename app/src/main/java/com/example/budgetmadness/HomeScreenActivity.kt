package com.example.budgetmadness

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var incomeTextView: TextView
    private lateinit var expenseTextView: TextView
    private lateinit var balanceTextView: TextView

    private val handler = Handler(Looper.getMainLooper())

    private val updateUIRunnable = object : Runnable {
        override fun run() {
            val incomeDb = IncomeDatabaseHelper(this@HomeScreenActivity)
            val expenseDb = BudgetDatabaseHelper(this@HomeScreenActivity)

            // Get values
            val latestIncome = incomeDb.getLatestIncome()
            val totalIncome = incomeDb.getTotalIncome()
            val latestExpense = expenseDb.getLatestExpense()

            // Display latest income and expense
            incomeTextView.text = "+R$latestIncome"
            expenseTextView.text = "-R$latestExpense"

            // Compute balance: totalIncome - latestExpense
            val balance = totalIncome - latestExpense
            balanceTextView.text = "R$balance"

            // Log UI update
            logEvent("UI updated: Income: $latestIncome, Expense: $latestExpense, Balance: $balance")

            // Refresh every 5 seconds
            handler.postDelayed(this, 5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        // Connect TextViews
        incomeTextView = findViewById(R.id.incomeTextView)
        expenseTextView = findViewById(R.id.textViewExpenses)
        balanceTextView = findViewById(R.id.textViewBalance)

        // Start UI updates
        handler.post(updateUIRunnable)

        // Bottom navigation
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateUIRunnable)
        logEvent("HomeScreenActivity destroyed")
    }

    private fun logEvent(message: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val logMessage = "$timestamp - $message"
        Log.d("HomeScreenActivityLog", logMessage)

        try {
            val file = File(getExternalFilesDir(null), "app_log.txt")
            val writer = FileWriter(file, true)
            writer.appendLine(logMessage)
            writer.close()
        } catch (e: IOException) {
            Log.e("HomeScreenActivityLog", "Failed to write log: ${e.message}")
        }
    }
}
