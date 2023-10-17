package com.example.fries_week4

import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.fries_week4.databinding.ActivityMapsBinding
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.Marker
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Locale

//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var myButton: MaterialButton
    private lateinit var recyclerView : RecyclerView
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButton = findViewById(R.id.button3)

        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST){}

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("Map: ", "Map ready")

        sharedPrefs  = getSharedPreferences("savedStuff", MODE_PRIVATE)
        val lastLat = sharedPrefs.getFloat("lastLat", 0.0f)
        val lastLong = sharedPrefs.getFloat("lastLong", 0.0f)
        if (lastLong != 0.0f && lastLat != 0.0f) {
            val geocoder = Geocoder(this, Locale.getDefault())
            val results = try {
                geocoder.getFromLocation(lastLat.toDouble(), lastLong.toDouble(), 1) // Limit results to 1 to get only the best result
            } catch (exception: Exception) {
                Log.e("Maps", "Geocoding failed", exception)
                listOf<Address>()
            }

            if (results.isNullOrEmpty()) {
                Log.e("Maps", "No addresses found")
            } else {
                val currentAddress = results[0]
                val country = currentAddress.countryName
                val it = LatLng(lastLat.toDouble(), lastLong.toDouble())

                marker = mMap.addMarker(MarkerOptions().position(it).title(country))!!
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 2f))

                myButton.visibility = View.VISIBLE
                myButton.text = "Results for $country"
                myButton.icon = ContextCompat.getDrawable(this, R.drawable.check)
                myButton.setBackgroundColor(getColor(R.color.green))

                recyclerView = findViewById(R.id.articlesRecyclerView)
                val articlesManager = ArticlesManager<Any>()
                val apiKey= getString(R.string.news_api_key)
                var articles = listOf<Article>()

                CoroutineScope(Dispatchers.IO).launch{
                    articles = articlesManager.retrieveArticles("$country&searchIn=title", "", "", apiKey, false)
                    withContext(Dispatchers.Main){
                        val adapter = ArticlesAdapter(articles)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@MapsActivity, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }
        }

        mMap.setOnMapLongClickListener {
            Log.d("Maps", "Long click at ${it.latitude}, ${it.longitude}")

         //   mMap.clear()

            val geocoder = Geocoder(this, Locale.getDefault())
            val results = try {
                geocoder.getFromLocation(it.latitude, it.longitude, 1) // Limit results to 1 to get only the best result
            } catch (exception: Exception) {
                Log.e("Maps", "Geocoding failed", exception)
                listOf<Address>()
            }

            if (results.isNullOrEmpty()) {
                Log.e("Maps", "No addresses found")
            } else {
                val currentAddress = results[0]
                val country = currentAddress.countryName
                sharedPrefs.edit().putFloat("lastLat", (it.latitude).toFloat()).apply()
                sharedPrefs.edit().putFloat("lastLong", (it.longitude).toFloat()).apply()

                if(marker != null){
                    marker.remove()
                }
                marker = mMap.addMarker(MarkerOptions().position(it).title(country))!!
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 5f))

                myButton.visibility = View.VISIBLE
                myButton.text = "Results for $country"
                myButton.icon = ContextCompat.getDrawable(this, R.drawable.check)
                myButton.setBackgroundColor(getColor(R.color.green))

                recyclerView = findViewById(R.id.articlesRecyclerView)
                val articlesManager = ArticlesManager<Any>()
                val apiKey= getString(R.string.news_api_key)
                var articles = listOf<Article>()

                CoroutineScope(Dispatchers.IO).launch{
                    articles = articlesManager.retrieveArticles("$country&searchIn=title", "", "", apiKey, false)

                    withContext(Dispatchers.Main){
                        val adapter = ArticlesAdapter(articles)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@MapsActivity, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }
        }

        // Add a marker in Sydney and move the camera
//        val latLng = LatLng(38.898365, -77.046753)
//        val title = "GWU"
//        mMap.addMarker(MarkerOptions().position(latLng).title(title))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    // CLASS STUFF
//    val okHttpClient = OkHttpClient()
//    val response=okHttpClient.newCall(request).execute()
//    val responseBody:String?=response.body?.string()
//    if(response.isSuccessful && !responseBody.isNullOrEmpty()){
//        val yelpList = mutableListOf<YelpBusiness>()
//        val json = JSONObject(responseBody)
//        val businesses = json.getJSONArray("businesses")
//        for(i in 0 < until < businesses.length()){
//            val currentBusiness=businesses.getJSONObject(i)
//            val name=currentBusiness.getString("name")
//            val rating=currentBusiness.getDouble("rating")
//            val icon=currentBusiness.getString("image_url")
//            val categories=currentBusiness.getJSONArray("categories")
//            val currentCategory=categories.getJSONObject(0)
//            val title=currentCategory.getString("title")
//            val yelp = YelpBusiness(
//            )
//        }
//    }

}