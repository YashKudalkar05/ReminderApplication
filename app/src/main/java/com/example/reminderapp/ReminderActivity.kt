package com.example.reminderapp

import android.provider.Settings
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar

class ReminderActivity : AppCompatActivity() {

    private val CHANNEL_ID = "REMINDER_CHANNEL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        // Call this to check and request notification permission
        checkNotificationPermission()

        // Set up the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve the intent extras
        val title = intent.getStringExtra("REMINDER_TITLE")
        val description = intent.getStringExtra("REMINDER_DESCRIPTION")
        val hour = intent.getIntExtra("REMINDER_HOUR", -1)
        val minute = intent.getIntExtra("REMINDER_MINUTE", -1)

        // Get the TextViews from the layout
        val tvReminderTitle = findViewById<TextView>(R.id.tvReminderTitle)
        val tvReminderDescription = findViewById<TextView>(R.id.tvReminderDescription)

        // Set the title
        tvReminderTitle.text = title

        // Format the time properly according to the locale
        val timeFormatted = String.format("%02d:%02d", hour, minute)

        // Set the description and time using the string resource
        tvReminderDescription.text = getString(R.string.reminder_details, description, timeFormatted)

        // Get current time
        val currentTime = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Create reminder time based on received hour and minute
        val reminderTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Calculate the time difference
        val timeDifference = reminderTime.timeInMillis - currentTime.timeInMillis

        // If less than or equal to 1 hour remains, show the notification
        if (timeDifference in 0..3600000) { // 1 hour = 3600000 milliseconds
            createNotificationChannel()
            showNotification(title, description, hour, minute)
        } else {
            Log.d("ReminderActivity", "Notification not triggered. Time difference is greater than 1 hour.")
        }
    }


    private fun showNotification(title: String?, description: String?, hour: Int, minute: Int) {
        // Create an intent to open the MainActivity when the notification is clicked
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        // Create the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your own icon
            .setContentTitle("Reminder: $title")
            .setContentText("$description\nScheduled for ${String.format("%02d:%02d", hour, minute)}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Show the notification
        NotificationManagerCompat.from(this).notify(1, notification)

    }



    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Channel"
            val descriptionText = "Channel for reminder notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

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
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.areNotificationsEnabled()) {
                // Request permission
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            }
        }
    }

}
