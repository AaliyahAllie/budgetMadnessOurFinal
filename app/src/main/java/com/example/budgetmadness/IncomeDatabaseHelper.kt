package com.example.budgetmadness

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class IncomeDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "income.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "income"
        const val COLUMN_ID = "_id"
        const val COLUMN_CASH = "cash_income"
        const val COLUMN_CARD = "card_income"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_CASH REAL, " +
                "$COLUMN_CARD REAL)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertIncome(cash: Double, card: Double) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CASH, cash)
            put(COLUMN_CARD, card)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getTotalIncome(): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_CASH + $COLUMN_CARD) FROM $TABLE_NAME", null)
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return total
    }
    fun getLatestIncome(): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_CASH, $COLUMN_CARD FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC LIMIT 1",
            null
        )
        var latestIncome = 0.0
        if (cursor.moveToFirst()) {
            val cash = cursor.getDouble(0)
            val card = cursor.getDouble(1)
            latestIncome = cash + card
        }
        cursor.close()
        db.close()
        return latestIncome
    }
    fun getAllIncomeHistory(): List<Pair<Double, Double>> {
        val db = this.readableDatabase
        val list = mutableListOf<Pair<Double, Double>>()
        val cursor = db.rawQuery("SELECT $COLUMN_CASH, $COLUMN_CARD FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC", null)

        while (cursor.moveToNext()) {
            val cash = cursor.getDouble(0)
            val card = cursor.getDouble(1)
            list.add(Pair(cash, card))
        }
        cursor.close()
        db.close()
        return list
    }


}
