package com.example.budgetmadness

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BudgetHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MONTH TEXT,
                $COLUMN_MIN REAL,
                $COLUMN_MAX REAL,
                $COLUMN_BUDGET REAL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertBudget(month: String, min: Double, max: Double, budget: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MONTH, month)
            put(COLUMN_MIN, min)
            put(COLUMN_MAX, max)
            put(COLUMN_BUDGET, budget)
        }
        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L
    }

    data class Budget(val month: String, val min: Double, val max: Double, val entered: Double)

    fun getBudgetForMonth(month: String): Budget? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_MONTH, COLUMN_MIN, COLUMN_MAX, COLUMN_BUDGET),
            "$COLUMN_MONTH = ?",
            arrayOf(month),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val min = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MIN))
            val max = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MAX))
            val entered = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BUDGET))
            cursor.close()
            Budget(month, min, max, entered)
        } else {
            cursor.close()
            null
        }
    }

    companion object {
        private const val DATABASE_NAME = "budget.db"
        private const val DATABASE_VERSION = 2 // Bumped version to recreate DB

        private const val TABLE_NAME = "budget"
        private const val COLUMN_ID = "id"
        private const val COLUMN_MONTH = "month"
        private const val COLUMN_MIN = "min_budget"
        private const val COLUMN_MAX = "max_budget"
        private const val COLUMN_BUDGET = "monthly_budget"
    }
}
