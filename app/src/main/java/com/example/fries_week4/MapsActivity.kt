package com.example.fries_week4

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.fries_week4.databinding.ActivityMapsBinding
import com.google.android.gms.maps.MapsInitializer
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var myButton: Button

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

        mMap.setOnMapLongClickListener {
            Log.d("Maps", "Long click at ${it.latitude}, ${it.longitude}")

         //   mMap.clear()

            val geocoder = Geocoder(this, Locale.getDefault())
            val results = try{
                geocoder.getFromLocation(it.latitude, it.longitude, 10)
            }catch(exception: Exception){
                Log.e("Maps", "Geocoding failed", exception)
                listOf<Address>()
            }

            if(results.isNullOrEmpty()){
                Log.e("Maps", "No addresses found")
            }else{
                val currentAddress = results[0]
                val addressLine = currentAddress.getAddressLine(0)

                mMap.addMarker(MarkerOptions().position(it).title(addressLine))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(it))

                myButton.text=addressLine
            }
        }

        // Add a marker in Sydney and move the camera
        val latLng = LatLng(38.898365, -77.046753)
        val title = "GWU"
        mMap.addMarker(MarkerOptions().position(latLng).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }
}