package com.example.budgetmadness

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseHistoryAdapter(private val data: List<BudgetDatabaseHelper.ExpenseEntry>) :
    RecyclerView.Adapter<ExpenseHistoryAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return ExpenseViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = data[position]
        holder.view.text = "Name: ${expense.name}, Amount: R${expense.amount}, Date: ${expense.date}"
    }

    override fun getItemCount(): Int = data.size
}
