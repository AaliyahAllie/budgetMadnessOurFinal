package com.example.budgetmadness

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddExpensesActivity : AppCompatActivity() {

    private lateinit var expenseNameInput: EditText
    private lateinit var expenseAmountInput: EditText
    private lateinit var paymentMethodInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var selectDataButton: Button
    private lateinit var uploadReceiptButton: Button
    private lateinit var addExpenseButton: Button

    private var selectedDate: String = ""
    private val TAG = "AddExpensesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_expenses)

        Log.d(TAG, "AddExpensesActivity started")
        logToFile("AddExpensesActivity started")

        val dbHelper = BudgetDatabaseHelper(this)

        // LINK VIEWS
        expenseNameInput = findViewById(R.id.expenseNameInput)
        expenseAmountInput = findViewById(R.id.expenseAmountInput)
        paymentMethodInput = findViewById(R.id.paymentMethodInput)
        categorySpinner = findViewById(R.id.categorySpinner)
        selectDataButton = findViewById(R.id.selectDateButton)
        uploadReceiptButton = findViewById(R.id.uploadReceiptButton)
        addExpenseButton = findViewById(R.id.addExpenseButton)

        // SPINNER SETUP
        val categories = dbHelper.getAllCategories()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // DATE PICKER
        selectDataButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                selectedDate = "$y-${m + 1}-$d"
                selectDataButton.text = selectedDate
                logToFile("Date selected: $selectedDate")
            }, year, month, day)

            datePickerDialog.show()
        }

        // ADD EXPENSE BUTTON
        addExpenseButton.setOnClickListener {
            val name = expenseNameInput.text.toString()
            val amount = expenseAmountInput.text.toString().toDoubleOrNull() ?: 0.0
            val paymentMethod = paymentMethodInput.text.toString()
            val category = categorySpinner.selectedItem?.toString() ?: ""
            val date = selectedDate

            val logMsg = "Attempting to add expense - Name: $name, Amount: $amount, Method: $paymentMethod, Category: $category, Date: $date"
            Log.d(TAG, logMsg)
            logToFile(logMsg)

            if (name.isNotEmpty() && amount > 0 && paymentMethod.isNotEmpty() && category.isNotEmpty() && date.isNotEmpty()) {
                dbHelper.insertExpense(name, amount, paymentMethod, category, date)
                Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Expense added successfully")
                logToFile("Expense added successfully")
                clearInputs()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Failed to add expense - missing input")
                logToFile("Failed to add expense - missing input")
            }
        }

        // Bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_income -> {
                    startActivity(Intent(this, IncomeActivity::class.java))
                    true
                }
                R.id.nav_home -> {
                    startActivity(Intent(this, StarterPageActivity::class.java))
                    true
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, AddExpensesActivity::class.java))
                    true
                }
                R.id.nav_open_menu -> {
                    startActivity(Intent(this, MenuActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun clearInputs() {
        expenseNameInput.text.clear()
        expenseAmountInput.text.clear()
        paymentMethodInput.text.clear()
        categorySpinner.setSelection(0)
        selectDataButton.text = "Select Date"
        selectedDate = ""
        Log.d(TAG, "Inputs cleared")
        logToFile("Inputs cleared")
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
