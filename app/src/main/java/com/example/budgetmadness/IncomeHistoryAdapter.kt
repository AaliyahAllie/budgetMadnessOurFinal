package com.example.budgetmadness

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IncomeHistoryAdapter(private val data: List<Pair<Double, Double>>) :
    RecyclerView.Adapter<IncomeHistoryAdapter.IncomeViewHolder>() {

    class IncomeViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return IncomeViewHolder(textView)
    }

    override fun onBindViewHolder(holder: IncomeViewHolder, position: Int) {
        val (cash, card) = data[position]
        holder.view.text = "Cash: R%.2f, Card: R%.2f".format(cash, card)
    }

    override fun getItemCount(): Int = data.size
}
