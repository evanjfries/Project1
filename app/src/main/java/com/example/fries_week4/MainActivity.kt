package com.example.fries_week4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var myList: ListView
    private lateinit var mapButton: Button
    private lateinit var headlinesButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapButton = findViewById(R.id.mapButton)
        headlinesButton = findViewById(R.id.headlinesButton)

        val sharedPrefs  = getSharedPreferences("savedStuff", MODE_PRIVATE)

        mapButton.setOnClickListener(View.OnClickListener { // Create an Intent to switch to the target activity
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        })

        headlinesButton.setOnClickListener(View.OnClickListener { // Create an Intent to switch to the target activity
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        })
    }
}