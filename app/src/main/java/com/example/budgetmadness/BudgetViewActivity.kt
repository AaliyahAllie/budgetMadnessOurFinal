package com.example.budgetmadness

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class BudgetViewActivity : AppCompatActivity() {

    private lateinit var viewMonthSpinner: Spinner
    private lateinit var viewButton: Button
    private lateinit var budgetDetailsText: TextView
    private lateinit var budgetHandler: BudgetHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_view)

        viewMonthSpinner = findViewById(R.id.viewMonthSpinner)
        viewButton = findViewById(R.id.viewButton)
        budgetDetailsText = findViewById(R.id.budgetDetailsText)

        val months = resources.getStringArray(R.array.months_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        viewMonthSpinner.adapter = adapter

        budgetHandler = BudgetHandler(this)

        viewButton.setOnClickListener {
            val selectedMonth = viewMonthSpinner.selectedItem.toString()
            val budget = budgetHandler.getBudgetForMonth(selectedMonth)

            if (budget != null) {
                budgetDetailsText.text = """
                    Month: $selectedMonth
                    Min Budget: R${budget.min}
                    Max Budget: R${budget.max}
                    Entered Budget: R${budget.entered}
                """.trimIndent()
            } else {
                budgetDetailsText.text = "No budget found for $selectedMonth."
            }
        }
    }
}
