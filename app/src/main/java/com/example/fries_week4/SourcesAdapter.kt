package com.example.fries_week4

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SourcesAdapter (val sources: List<Source>, val searchTerm : String): RecyclerView.Adapter<SourcesAdapter.ViewHolder>() {
    private lateinit var context : Context
    class ViewHolder(rootLayout: View): RecyclerView.ViewHolder(rootLayout){
        var title : TextView = rootLayout.findViewById(R.id.sourceTitleText)
        var description : TextView = rootLayout.findViewById(R.id.sourceDescriptionText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("RV", "inside onCreateViewHolder")
        context = parent.context
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val rootLayout: View = layoutInflater.inflate(R.layout.sourcescardviewlayout, parent, false)
        val viewHolder=ViewHolder(rootLayout)
        return viewHolder
    }

    override fun getItemCount(): Int {
        Log.d("RV", "inside getItemCount")
        return sources.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("RV", "inside onBindViewHolder")
        val currentSource = sources[position]
        holder.title.text = currentSource.title
        holder.description.text=currentSource.description

        holder.itemView.setOnClickListener {
            val newIntent = Intent(holder.itemView.context, ArticlesActivity::class.java)
            newIntent.putExtra("searchTerm", searchTerm)
            newIntent.putExtra("source", currentSource.id)
            newIntent.putExtra("sourceName", currentSource.title)
            holder.itemView.context.startActivity(newIntent)
        }
    }
}