package com.example.fries_week4
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SourcesManager<PackageManager> {
    val okHttpClient: OkHttpClient;
    init{
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        okHttpClient = builder.build()
    }

    fun retrieveSources(category: String, apiKey:String): List<Source>{
        var url = "https://newsapi.org/v2/top-headlines/sources?"
        if(!category.isNullOrEmpty()){url+="category=$category&"}
        url += "apiKey=$apiKey&language=en"
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
            val sourceList = mutableListOf<Source>()
            val json = JSONObject(responseBody)
            val sources = json.getJSONArray("sources")
            for (i in 0 until sources.length()){
                val currentSource = sources.getJSONObject(i)
                var title = currentSource.getString("name")
                var description = currentSource.getString("description")
                var id = currentSource.getString("id")

                val source = Source(
                    title=title,
                    description=description,
                    id=id
                )
                sourceList.add(source)
            }
            return sourceList
        }else{
            return listOf()
        }

//        fun retrieveSources(val articles : List<Article>){
//
//        }
    }
}