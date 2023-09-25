package com.example.fries_week4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity2 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        recyclerView = findViewById(R.id.recyclerView)

        val yelps = getFakeYelpData()
        val adapter = YelpAdapter(yelps)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =LinearLayoutManager(this)
    }

    private fun getFakeYelpData(): List<YelpBusiness>{
        return listOf(
            YelpBusiness("Panera","Breakfast","4 stars","https://..."),
            YelpBusiness("WingsToGo","Comfort","5 stars ","https://..."),
            YelpBusiness("Log Cabin","Seafood","4  stars","https://..."),
            YelpBusiness("Dunkin Donut","Breakfast","3  stars","https://..."),
            YelpBusiness("Starbucks","Coffee","3  stars","https://..."),
            YelpBusiness("Panera","Breakfast","3 stars","https://..."),
            YelpBusiness("Panera","Breakfast","3 stars","https://..."),
            YelpBusiness("Panera","Breakfast","3 stars","https://..."),
            YelpBusiness("Panera","Breakfast","3 stars","https://..."),
            YelpBusiness(icon="https://....", category="Dinner", rating="3 stars", restaurantName ="Subway")
        )
    }
}