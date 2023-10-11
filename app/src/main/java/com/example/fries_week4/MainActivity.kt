package com.example.fries_week4

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mapButton: Button
    private lateinit var headlinesButton: Button
    private lateinit var searchButton: Button
    private lateinit var editText: EditText
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain Views
        mapButton = findViewById(R.id.mapButton)
        headlinesButton = findViewById(R.id.headlinesButton)
        searchButton = findViewById(R.id.searchButton)
        editText = findViewById(R.id.searchEditText)

        // Get shared preferences
        sharedPrefs  = getSharedPreferences("savedStuff", MODE_PRIVATE)
        val lastSearchTerm = sharedPrefs.getString("lastSearchTerm", "")
        editText.setText(lastSearchTerm)
        if (lastSearchTerm != null) {
            if (lastSearchTerm.isNotEmpty()) searchButton.isEnabled = true
        }

        // Search buttons
        mapButton.setOnClickListener(View.OnClickListener { // Create an Intent to switch to the target activity
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        })

        headlinesButton.setOnClickListener(View.OnClickListener { // Create an Intent to switch to the target activity
            val intent = Intent(this@MainActivity, ArticlesActivity::class.java)
            startActivity(intent)
        })

        searchButton.setOnClickListener(View.OnClickListener {
            val searchTerm = editText.text.toString()
            sharedPrefs.edit().putString("lastSearchTerm", searchTerm).apply()
            val intent = Intent(this@MainActivity, SourcesActivity::class.java)
            intent.putExtra("searchTerm", searchTerm)
            startActivity(intent)
            Log.d("Search term", searchTerm)
        })

        // TextWatcher to enable search button
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val hasText = s?.isNotEmpty() == true
                searchButton.isEnabled = hasText
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }
}