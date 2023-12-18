package com.example.fries_week4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticlesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var topHeadlines : Boolean = true
    private lateinit var pageTitle : TextView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        toolbar = findViewById(R.id.toolbar5)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home Page"

        var searchTerm = intent.getStringExtra("searchTerm")
        var sourceName = intent.getStringExtra("sourceName")
        if(!searchTerm.isNullOrEmpty()&& sourceName.isNullOrEmpty()){
            supportActionBar?.title = "Search for: '$searchTerm'"
        }
        if(!searchTerm.isNullOrEmpty()&& !sourceName.isNullOrEmpty()){
            supportActionBar?.title = "Search for: '$searchTerm' from $sourceName"
        }

        recyclerView = findViewById(R.id.articlesRecyclerView)
        val articlesManager = ArticlesManager<Any>()
        val apiKey= getString(R.string.news_api_key)
        var articles = listOf<Article>()
        if(!intent.getStringExtra("searchTerm").isNullOrEmpty()) topHeadlines = false

        CoroutineScope(IO).launch{
            articles = articlesManager.retrieveArticles(intent.getStringExtra("searchTerm"), intent.getStringExtra("source"), "", apiKey, topHeadlines)
            if(articles.isNullOrEmpty()){
                runOnUiThread{Toast.makeText(this@ArticlesActivity, "No articles found with these parameters", Toast.LENGTH_SHORT).show()}
            }else{
                withContext(Main){
                    val adapter = ArticlesAdapter(articles)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager =LinearLayoutManager(this@ArticlesActivity)
                }
            }
        }
    }
}