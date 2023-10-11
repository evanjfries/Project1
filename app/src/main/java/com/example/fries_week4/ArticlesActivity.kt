package com.example.fries_week4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        pageTitle = findViewById(R.id.searchDescriptionText)
        var searchTerm = intent.getStringExtra("searchTerm")
        var sourceName = intent.getStringExtra("sourceName")
        if(!searchTerm.isNullOrEmpty()&& sourceName.isNullOrEmpty()) pageTitle.text = "Search for: '$searchTerm'"
        if(!searchTerm.isNullOrEmpty()&& !sourceName.isNullOrEmpty()) pageTitle.text = "Search for: '$searchTerm' from $sourceName"

        recyclerView = findViewById(R.id.articlesRecyclerView)
        val articlesManager = ArticlesManager<Any>()
        val apiKey= getString(R.string.news_api_key)
        var articles = listOf<Article>()
        if(!intent.getStringExtra("searchTerm").isNullOrEmpty()) topHeadlines = false

        CoroutineScope(IO).launch{
            articles = articlesManager.retrieveArticles(intent.getStringExtra("searchTerm"), intent.getStringExtra("source"), apiKey, topHeadlines)

            withContext(Main){
                val adapter = ArticlesAdapter(articles)
                recyclerView.adapter = adapter
                recyclerView.layoutManager =LinearLayoutManager(this@ArticlesActivity)
            }
        }
    }
}