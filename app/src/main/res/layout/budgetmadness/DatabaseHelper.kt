package com.example.budgetmadness

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "budget_madness.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_FIRST_NAME = "first_name"
        private const val COLUMN_LAST_NAME = "last_name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_USERNAME TEXT PRIMARY KEY, "
                + "$COLUMN_PASSWORD TEXT, "
                + "$COLUMN_FIRST_NAME TEXT, "
                + "$COLUMN_LAST_NAME TEXT, "
                + "$COLUMN_EMAIL TEXT, "
                + "$COLUMN_PHONE TEXT)"
                )
        db?.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(username: String, password: String, firstName: String, lastName: String, email: String, phone: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_FIRST_NAME, firstName)
        values.put(COLUMN_LAST_NAME, lastName)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PHONE, phone)

        db.insert(TABLE_USERS, null, values)
        db.close()
    }

    // Method to check if the username and password match a record in the database
    fun validateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?", arrayOf(username, password))

        val isValid = cursor.count > 0  // If a match is found, the cursor count will be greater than 0
        cursor.close()
        db.close()

        return isValid
    }
    //method to save user images
    fun updateUser(username: String, firstName: String, lastName: String, email: String, phone: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
        }
        val result = db.update(TABLE_USERS, values, "$COLUMN_USERNAME = ?", arrayOf(username))
        db.close()
        return result > 0
    }

}
