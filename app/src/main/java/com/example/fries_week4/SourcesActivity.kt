package com.example.fries_week4

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient

class SourcesActivity : AppCompatActivity() {

    private lateinit var searchTermText: TextView
    private lateinit var categorySpinner: Spinner
    private lateinit var recyclerView: RecyclerView

//    private lateinit var newsApiClient: NewsApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)

        val client = OkHttpClient()

        //Get Search Term
        searchTermText = findViewById(R.id.searchTermText)
        categorySpinner = findViewById(R.id.categorySpinner)
        val searchTerm = intent.getStringExtra("searchTerm")
        if (searchTerm != null) {
            searchTermText.text = "Search for: '$searchTerm'"
        }

        // Populate the Spinner with categories
        val categories = arrayOf("General", "Business", "Health", "Science", "Technology", "Entertainment", "Sports")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Set up a listener for the selected category
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                val apiKey= getString(R.string.news_api_key)
                var sources = listOf<Source>()
                val sourcesManager = SourcesManager<Any>()

                CoroutineScope(Dispatchers.IO).launch{
                    sources = searchTerm?.let { sourcesManager.retrieveSources(selectedCategory.lowercase(), apiKey) }!!

                    withContext(Dispatchers.Main){
                        val searchTerm = intent.getStringExtra("searchTerm")
                        val adapter = searchTerm?.let { SourcesAdapter(sources, it) }
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@SourcesActivity)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected (if necessary)
            }
        }

        recyclerView = findViewById(R.id.sourcesRecyclerView)
        val sourcesManager = SourcesManager<Any>()
        val apiKey= getString(R.string.news_api_key)
        var sources = listOf<Source>()

        CoroutineScope(Dispatchers.IO).launch{
            sources = searchTerm?.let { sourcesManager.retrieveSources("", apiKey) }!!

            withContext(Dispatchers.Main){
                val adapter = SourcesAdapter(sources, searchTerm)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this@SourcesActivity)
            }
        }



    }
}