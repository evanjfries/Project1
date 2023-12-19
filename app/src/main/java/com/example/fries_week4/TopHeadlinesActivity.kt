package com.example.fries_week4

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
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
    private lateinit var nextButton : Button
    private lateinit var prevButton: Button
    private lateinit var pageText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_headlines)
        var pageNumber=1
        var articles = listOf<Article>()

        categorySpinner = findViewById(R.id.spinner)
        recyclerView = findViewById(R.id.articlesRecyclerView)
        nextButton=findViewById(R.id.nextButton)
        prevButton=findViewById(R.id.prevButton)
        pageText=findViewById(R.id.pageText)

        toolbar = findViewById(R.id.toolbar4)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Top Headlines: General"

        sharedPrefs  = getSharedPreferences("savedStuff", MODE_PRIVATE)
        var lastCategory = sharedPrefs.getString("lastCategory", "")
        if(!lastCategory.isNullOrEmpty()){
            supportActionBar?.title = "Top Headlines: $lastCategory"
            lastCategory="General"
        }

        nextButton.setOnClickListener(){
            pageNumber++
            val articlesManager = ArticlesManager<Any>()
            val apiKey= getString(R.string.news_api_key)
            CoroutineScope(Dispatchers.IO).launch{
                withContext(Dispatchers.Main){
                    val pageSize=10
                    val numPages = (articles.size/pageSize).toInt()
                    pageText.text = "Page $pageNumber of $numPages"
                    if(pageNumber==numPages){
                        nextButton.isEnabled=false
                    }
                    if(pageNumber>1){
                        prevButton.isEnabled=true
                    }
                    val startIndex = (pageNumber-1) * pageSize
                    val endIndex = Integer.min(startIndex + pageSize, articles.size)
                    println("Articles size: ${articles.size}")
                    val pageArticles = articles.subList(startIndex, endIndex)
                    val adapter = ArticlesAdapter(pageArticles)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                }
            }
        }

        prevButton.setOnClickListener(){
            pageNumber--
            val articlesManager = ArticlesManager<Any>()
            val apiKey= getString(R.string.news_api_key)
            CoroutineScope(Dispatchers.IO).launch{
                withContext(Dispatchers.Main){
                    val pageSize=10
                    val numPages = (articles.size/pageSize).toInt()
                    pageText.text = "Page $pageNumber of $numPages"
                    if(pageNumber==1){
                        prevButton.isEnabled=false
                    }
                    if(pageNumber<pageSize){
                        nextButton.isEnabled=true
                    }
                    val startIndex = (pageNumber-1) * pageSize
                    val endIndex = Integer.min(startIndex + pageSize, articles.size)
                    val pageArticles = articles.subList(startIndex, endIndex)
                    val adapter = ArticlesAdapter(pageArticles)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                }
            }
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
                        pageNumber=1
                        val pageSize=10
                        prevButton.isEnabled=false
                        nextButton.isEnabled=true
                        val numPages = (articles.size/pageSize).toInt()
                        pageText.text = "Page $pageNumber of $numPages"
                        val startIndex = (pageNumber - 1) * pageSize
                        val endIndex = Integer.min(startIndex + pageSize, articles.size)
                        val pageArticles = articles.subList(startIndex, endIndex)
                        val adapter = ArticlesAdapter(pageArticles)
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
                        pageNumber=1
                        val pageSize=10
                        val numPages = (articles.size/pageSize).toInt()
                        prevButton.isEnabled=false
                        nextButton.isEnabled=true
                        pageText.text = "Page $pageNumber of $numPages"
                        val startIndex = (pageNumber - 1) * pageSize
                        val endIndex = Integer.min(startIndex + pageSize, articles.size)
                        val pageArticles = articles.subList(startIndex, endIndex)
                        val adapter = ArticlesAdapter(pageArticles)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@TopHeadlinesActivity)
                    }
                }
            }
        }

    }
}