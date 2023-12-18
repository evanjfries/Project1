package com.example.fries_week4

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopHeadlinesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categorySpinner: Spinner
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_headlines)

        categorySpinner = findViewById(R.id.spinner)
        recyclerView = findViewById(R.id.articlesRecyclerView)

        toolbar = findViewById(R.id.toolbar4)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Top Headlines: General"

        sharedPrefs  = getSharedPreferences("savedStuff", MODE_PRIVATE)
        var lastCategory = sharedPrefs.getString("lastCategory", "")
        if(!lastCategory.isNullOrEmpty()){
            supportActionBar?.title = "Top Headlines: $lastCategory"
        }

        // Populate the Spinner with categories
        val categories = arrayOf("General", "Business", "Health", "Science", "Technology", "Entertainment", "Sports")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
        if(lastCategory.isNullOrEmpty()){
            lastCategory = "General"
        }

        val desiredPosition = adapter.getPosition(lastCategory)
        if (desiredPosition != -1) {
            categorySpinner.setSelection(desiredPosition)
        }

        val articlesManager = ArticlesManager<Any>()
        val apiKey= getString(R.string.news_api_key)
        var articles = listOf<Article>()

        // Set up a listener for the selected category
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                sharedPrefs.edit().putString("lastCategory", selectedCategory).apply()
                supportActionBar?.title = "Top Headlines: $selectedCategory"
                val apiKey= getString(R.string.news_api_key)
                var sources = listOf<Source>()
                val sourcesManager = SourcesManager<Any>()

                CoroutineScope(Dispatchers.IO).launch{
                    articles = articlesManager.retrieveArticles("", "", selectedCategory.lowercase(), apiKey, true)

                    withContext(Dispatchers.Main){
                        val adapter = ArticlesAdapter(articles)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected (if necessary)
                CoroutineScope(Dispatchers.IO).launch{
                    articles = articlesManager.retrieveArticles("", "", lastCategory.lowercase(), apiKey, true)

                    withContext(Dispatchers.Main){
                        val adapter = ArticlesAdapter(articles)
                        Log.d("adapter", "attached")
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                    }
                }
            }
        }

    }
}