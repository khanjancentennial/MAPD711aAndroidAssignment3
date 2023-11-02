// Team: Bhargav Borse - 301278352 and Khanjan Dave - 301307330
// Assignment 3
// File: CityDetailsActivity.kt
// Description: This activity is part of an Android app designed to simplify the search for pizza restaurants. Users can choose from a list of cities, including Toronto, Scarborough, Mississauga, Oakville, and North York, and a few personal favorites from different regions. This activity displays the details of the selected city and uses Google Maps to show markers for pizza restaurants in that city. It makes it easy for users to find and enjoy delicious pizza wherever they are.


package com.example.bhargav_khanjan_assignment3

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapResultActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var cityName = ""

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_result)

        // Initialize SharedPreferences to retrieve the selected city
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val selectedCity = sharedPreferences.getString("selectedCity", "Toronto")

        // Set the text of the cityHeading TextView to the selected city name.
        val cityHeading = findViewById<TextView>(R.id.cityHeading)
        cityHeading.text = selectedCity
        cityName = selectedCity.toString()

        // Initialize Google Maps fragment to show pizza restaurant locations.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

//        val mapViewSwitch = findViewById<Switch>(R.id.mapViewSwitch)
//        mapViewSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE // Switch to Satellite View
//            } else {
//                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL // Switch to Standard View
//            }
//        }

        val mapViewSpinner = findViewById<Spinner>(R.id.mapViewSpinner)
        val mapTypes = resources.getStringArray(R.array.map_view_types)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mapTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mapViewSpinner.adapter = adapter

        mapViewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                    1 -> mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                    2 -> mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected
            }
        }

// Set the default selection to "Normal" (position 0)
        mapViewSpinner.setSelection(0)


        mapFragment.getMapAsync(this)



    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val cityLatLng = getCityLatLng(cityName)

        if (cityLatLng != null) {


            val zoomLevel = 12.5f

            // Create a CameraPosition and set the target (city's LatLng) and zoom level
            val cameraPosition = CameraPosition.Builder()
                .target(cityLatLng)
                .zoom(zoomLevel)
                .build()
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cityLatLng))



        }

        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null // Use the default window frame
            }

            @SuppressLint("MissingInflatedId")
            override fun getInfoContents(marker: Marker): View? {
                val infoView = layoutInflater.inflate(R.layout.marker_info_window, null)
                val title = infoView.findViewById<TextView>(R.id.titleTextView)
                val snippet = infoView.findViewById<TextView>(R.id.snippetTextView)
                val image = infoView.findViewById<ImageView>(R.id.markerImage1)

                title.text = marker.title
                snippet.text = marker.snippet

                // Retrieve the image resource ID from the marker's tag
                val imageResId = marker.tag as Int
                // Set the image for the ImageView
                image.setImageResource(imageResId)

                return infoView
            }
        })
        when (cityName) {
            "Toronto" -> {
                // Add markers for pizza restaurants in Toronto
                addPizzaMarker(LatLng(43.65781818403827, -79.40172334325447), "Pizza Place 1","123 Main St, Toronto\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.toronto_image)
                addPizzaMarker(LatLng(43.657130, -79.380190), "Pizza Place 2","123 Main St, Toronto\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.toronto_image)
                // Add more markers as needed
            }
            "Scarborough" -> {
                // Add markers for pizza restaurants in Scarborough
                addPizzaMarker(LatLng(43.773977, -79.258810), "Pizza Place A","123 Main St, Toronto\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.toronto_image)
                addPizzaMarker(LatLng(43.799003, -79.253975), "Pizza Place B","123 Main St, Toronto\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.toronto_image)
                // Add more markers as needed
            }
            // Add more cases for other cities
            else -> {
                // Default case, you can add a marker for the city center, for example
                if (cityLatLng != null) {
                    addPizzaMarker(cityLatLng, "Pizza Place","123 Main St, Toronto\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.toronto_image)
                }
            }
        }

    }
    // Define LatLng coordinates for different cities
    private fun getCityLatLng(city: String): LatLng? {
        // Define LatLng coordinates for different cities
        return when (city) {
            "Toronto" -> LatLng(43.651070, -79.347015)
            "Scarborough" -> LatLng(43.773977, -79.258810)
            // Add LatLng for other cities
            else -> null
        }
    }

    private fun addPizzaMarker(position: LatLng, title: String, snippet:String,imageResId: Int) {
        // Add a marker for a pizza restaurant
//        mMap.addMarker(MarkerOptions().position(position).title(title))
        val markerOptions = MarkerOptions()
            .position(position)
            .title(title)
            .snippet(snippet)



        val marker = mMap.addMarker(markerOptions)


        // Store the image resource ID as the marker's tag
        if (marker != null) {
            marker.tag = imageResId
        }
    }

}
