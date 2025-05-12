package com.example.budgetmadness

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class StarterPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter_page)

        findViewById<Button>(R.id.btnGetStarted).setOnClickListener {
            // Optional: Show a toast
            Toast.makeText(this, "Get Started Clicked!", Toast.LENGTH_SHORT).show()

            // Navigate to HomeScreenActivity
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish() //Closes StarterPageActivity so user can't go back to it with back button
        }
    }
}
