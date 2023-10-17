package com.example.fries_week4
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.NonDisposableHandle.parent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArticlesManager<PackageManager> {
    val okHttpClient: OkHttpClient;
    init{
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        okHttpClient = builder.build()
    }

    fun retrieveArticles(searchTerm: String?, source: String?, category: String?, apiKey:String, topHeadlines: Boolean): List<Article>{
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd",  Locale.getDefault())
        var url = "https://newsapi.org/v2/"
        url += if(topHeadlines){"top-headlines?country=us"}else{"everything?"}
        if(!searchTerm.isNullOrEmpty())url+= ("q=$searchTerm")
        if(!category.isNullOrEmpty())url+= ("&category=$category")
        if(!source.isNullOrEmpty())url+= ("&sources=$source")
//        if(searchTerm.isNullOrEmpty() && source.isNullOrEmpty())url+="&"
        url += "&apiKey=$apiKey&language=en"
        val request = Request.Builder()
            .url(url)
            .header(
                "Authorization",
                "Bearer $apiKey"
            )
            .get()
            .build()
        val response = okHttpClient.newCall(request).execute()
        val responseBody:String?=response.body?.string()

        if(response.isSuccessful && !responseBody.isNullOrEmpty()){
            Log.d("Response: ", responseBody)
            val articleList = mutableListOf<Article>()
            val json = JSONObject(responseBody)
            val articles = json.getJSONArray("articles")
            for (i in 0 until articles.length()){
                val currentArticle = articles.getJSONObject(i)
                var title = currentArticle.getString("title")
                val source = JSONObject(currentArticle.getString("source")).getString("name")
                var description = currentArticle.getString("description")
                val urlToImage = currentArticle.getString("urlToImage")
                val url = currentArticle.getString("url")

                if(title.length >50){
                    title = title.substring(0, 47) + "..."
                }
                if(description.length >170){
                    description = description.substring(0, 167) + "..."
                }
                val article = Article(
                    title=title,
                    source=source,
                    description=description,
                    urlToImage=urlToImage,
                    url=url
                )
                articleList.add(article)
            }
            return articleList
        }else{
            return listOf()
        }

//        fun retrieveSources(val articles : List<Article>){
//
//        }
    }
}