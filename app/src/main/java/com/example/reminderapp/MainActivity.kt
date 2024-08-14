package com.example.reminderapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_main)

        // Set up the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Create Reminder"

        val etReminderTitle = findViewById<EditText>(R.id.etReminderTitle)
        val etReminderDescription = findViewById<EditText>(R.id.etReminderDescription)
        val timePicker = findViewById<TimePicker>(R.id.tpReminderTime)

        val btnSetReminder = findViewById<Button>(R.id.btnSetReminder)

        btnSetReminder.setOnClickListener {
            val title = etReminderTitle.text.toString()
            val description = etReminderDescription.text.toString()
            val hour = timePicker.hour
            val minute = timePicker.minute

            // Debugging the hour and minute values
            Log.d("MainActivity", "TimePicker hour: $hour")
            Log.d("MainActivity", "TimePicker minute: $minute")
            val intent = Intent(this, ReminderActivity::class.java).apply {
                putExtra("REMINDER_TITLE", title)
                putExtra("REMINDER_DESCRIPTION", description)
                putExtra("REMINDER_HOUR", hour)
                putExtra("REMINDER_MINUTE", minute)
            }
            startActivity(intent)
        }
    }
}
