package com.example.budgetmadness

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.InputStream

class ProfileActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var dbHelper: DatabaseHelper
    private val currentUsername = "user123" // Replace with actual logged-in user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)

        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val btnChangeImage = findViewById<Button>(R.id.btnChangeImage)
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnChangeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSave.setOnClickListener {
            val updated = dbHelper.updateUser(
                currentUsername,
                etFirstName.text.toString(),
                etLastName.text.toString(),
                etEmail.text.toString(),
                etPhone.text.toString()
            )
            Toast.makeText(
                this,
                if (updated) "Updated!" else "Failed to update",
                Toast.LENGTH_SHORT
            ).show()
        }
        //BOTTOM NAV
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri!!)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                findViewById<ImageView>(R.id.profileImage).setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
