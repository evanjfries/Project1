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
    private lateinit var myButton: Button
    private lateinit var countryClicked : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myList = findViewById(R.id.listView)
        myButton = findViewById(R.id.button)

        val sharedPrefs  = getSharedPreferences("savedStuff", MODE_PRIVATE)

        var countryList = resources.getStringArray(R.array.countries)
        var arrayAdapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, countryList)
        myList.adapter = arrayAdapter

        myList.setOnItemClickListener { parent, view, position, id ->
            countryClicked = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "You selected $countryClicked", Toast.LENGTH_LONG).show()
            Log.d("country", "position $countryClicked")
        }

        myButton.setOnClickListener(View.OnClickListener { // Create an Intent to switch to the target activity
            val country : String = countryClicked
            sharedPrefs.edit().putString("COUNTRY", countryClicked).apply()

            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        })
    }
}