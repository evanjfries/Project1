package com.example.fries_week4

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArticlesAdapter (val articles: List<Article>): RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {
    private lateinit var context : Context
    class ViewHolder(rootLayout: View): RecyclerView.ViewHolder(rootLayout){
//        val articleNameText : TextView = rootLayout.findViewById(R.id.restaurant_name)
//        val categoryText : TextView = rootLayout.findViewById(R.id.Category)
//        val rating: TextView = rootLayout.findViewById(R.id.rating)
//        val icon: ImageView = rootLayout.findViewById(R.id.icon)
        var title : TextView = rootLayout.findViewById(R.id.sourceTitleText)
        var description : TextView = rootLayout.findViewById(R.id.sourceDescriptionText)
        var source : TextView = rootLayout.findViewById(R.id.sourceText)
        var icon : ImageView = rootLayout.findViewById(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("RV", "inside onCreateViewHolder")
        context = parent.context
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.articlescardviewlayout, parent, false)
        val viewHolder=ViewHolder(rootLayout)
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d("RV", "inside getItemCount")
        return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("RV", "inside onBindViewHolder")
        val currentArticle = articles[position]
        holder.title.text = currentArticle.title
        holder.source.text=currentArticle.source
        holder.description.text=currentArticle.description
        Glide.with(context)
            .load(currentArticle.urlToImage) // URL of the image
            .into(holder.icon) // ImageView to display the image

        holder.itemView.setOnClickListener {
            currentArticle.url?.let { url ->
                openUrlInBrowser(url)
            }
        }
    }

    private fun openUrlInBrowser(url: String) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(webIntent)
    }
}