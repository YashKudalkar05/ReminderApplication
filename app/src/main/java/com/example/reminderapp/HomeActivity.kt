package com.example.reminderapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    // Inside MainActivity.kt
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                // Navigate to HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_main -> {
                // Navigate to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val btnCreateReminder = findViewById<Button>(R.id.btnCreateReminder)

        // Set title and description
        tvTitle.text = "Reminder App"
        tvDescription.text = "This app allows you to create and view reminders. Use the buttons below to navigate to different features."

        btnCreateReminder.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
