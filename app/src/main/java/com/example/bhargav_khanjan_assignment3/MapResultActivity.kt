// Team: Bhargav Borse - 301278352 and Khanjan Dave - 301307330
// Assignment 3
// File: CityDetailsActivity.kt
// Description: This activity is part of an Android app designed to simplify the search for pizza restaurants. Users can choose from a list of cities, including Toronto, Scarborough, Mississauga, Oakville, and North York, and a few personal favorites from different regions. This activity displays the details of the selected city and uses Google Maps to show markers for pizza restaurants in that city. It makes it easy for users to find and enjoy delicious pizza wherever they are.

// Bhargav - set map and select different map view using spinner
// Khanjan - set the co-ordinates of the selected city, add marker in that co-ordinates and display info window with details

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


        val mapViewSpinner = findViewById<Spinner>(R.id.mapViewSpinner)
        val mapTypes = resources.getStringArray(R.array.map_view_types)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mapTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mapViewSpinner.adapter = adapter

        // select options from spinner
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

            // zoom to the selected city
            val zoomLevel = 12.5f

            // Create a CameraPosition and set the target (city's LatLng) and zoom level
            val cameraPosition = CameraPosition.Builder()
                .target(cityLatLng)
                .zoom(zoomLevel)
                .build()
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cityLatLng))

        }

        // show details in the info window after clicking on marker
        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null // Use the default window frame
            }

            @SuppressLint("MissingInflatedId")
            override fun getInfoContents(marker: Marker): View? {

                // bind UI
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

        // show 5 pizza stores based on selected city
        when (cityName) {
            "Toronto" -> {
                // Add markers for pizza restaurants in Toronto
                addPizzaMarker(LatLng(43.65097052139968, -79.37833182924827), "Subway","127 Yonge St, Toronto, ON M5C 1W5\nOpen: Mon-Sat 08 AM - 11 PM", R.drawable.subway_toronto)
                addPizzaMarker(LatLng(43.65101180347537, -79.38144002516066), "Za Cafe Pizzeria & Bar","372 Bay St., Toronto, ON M5H 2W9\nOpen: Mon-Sat 11 AM - 12 AM", R.drawable.zacafe_toronto)
                addPizzaMarker(LatLng(43.64992923466573, -79.37860749764047), "Pizzaiolo","104 Yonge St, Toronto, ON M5C 2Y6\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.pizzaiolo_toronto)
                addPizzaMarker(LatLng(43.65091734477355, -79.37849415558088), "Mamma's Pizza","127 Yonge St, Toronto, ON M5C 1W4\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.mammaspizza_toronto)
                addPizzaMarker(LatLng(43.65231832957128, -79.37491107001834), "Domino's Pizza","67 Richmond St E, Toronto, ON M5C 1N9\nOpen: Mon-Sat 11 AM - 01 AM", R.drawable.domino_toronto)
            }
            "Scarborough" -> {
                // Add markers for pizza restaurants in Scarborough
                addPizzaMarker(LatLng(43.79581860068014, -79.23168857668689), "Popular Pizza","5790 Sheppard Ave E Unit 2, Scarborough, ON M1B 5J6\nOpen: Mon-Sat 11 AM - 02 AM", R.drawable.popular_scarborough)
                addPizzaMarker(LatLng(43.787539187313996, -79.26682142624061), "Pizza Hut Scarborough","4455 Sheppard Ave E Unit 3, Scarborough, ON M1S 3G9\nOpen: Mon-Sat 11 AM - 11 PM", R.drawable.pizzahut_scarborough)
                addPizzaMarker(LatLng(43.76668329243716, -79.28237851145823), "Pizza On Fire","880 Ellesmere Rd Unit 10, Scarborough, ON M1P 2L8\nOpen: Mon-Sat 11 AM - 11 PM", R.drawable.pizzafire_scarborough)
                addPizzaMarker(LatLng(43.785628211220626, -79.27936102146033), "Pizza Nova","4198 Sheppard Ave E, Scarborough, ON M1S 1T3\nOpen: Mon-Sat 11 AM - 12 AM", R.drawable.pizzanova_scarborough)
                addPizzaMarker(LatLng(43.80748882470899, -79.17606109536985), "Pizza Pizza","9390 Sheppard Ave E, Scarborough, ON M1B 5R5\nOpen: Mon-Sat 11 AM - 12 AM", R.drawable.pizza_scarborough)
            }
            "Mississauga" -> {
                // Add markers for pizza restaurants in Mississauga
                addPizzaMarker(LatLng(43.6179696453121, -79.59542311780226), "Luca Pizza","3415 Dixie Rd, Mississauga, ON L4Y 4J6\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.luca_missi)
                addPizzaMarker(LatLng(43.59217521248162, -79.62600075365361), "Pizza Pizza","1585 Mississauga Vly Blvd, Mississauga, ON L5A 3W9\nOpen: Mon-Sat 11 AM - 12 AM", R.drawable.pizza_missi)
                addPizzaMarker(LatLng(43.598590905531914, -79.66070563006294), "3 For 2 Pizza & Wings","1585 Mississauga Vly Blvd, Mississauga, ON L5A 3W9\nOpen: Mon-Sat 11 AM - 12 AM", R.drawable.twoforthree_missi)
                addPizzaMarker(LatLng(43.590798947159065, -79.67983588400692), "Sauce n Slice Pizza","1010 Dream Crest Rd #1, Mississauga, ON L5V 3A4\nOpen: Mon-Sat 11 AM - 11 PM", R.drawable.saucenslice_missi)
                addPizzaMarker(LatLng(43.602765697650476, -79.62678505620732), "Gino's Pizza","1585 Mississauga Vly Blvd, Mississauga, ON L5A 3W9\nOpen: Mon-Sat 11 AM - 11 PM", R.drawable.gino_missi)
            }
            "Oakville" -> {
                // Add markers for pizza restaurants in Oakville
                addPizzaMarker(LatLng(43.44903429464671, -79.70291563948082), "Topper's Pizza - Oakville","220 North Service Rd W, Oakville, ON L6M 2T3\nOpen: Mon-Sat 11 AM - 11 PM", R.drawable.toppers_oakville)
                addPizzaMarker(LatLng(43.44285694988649, -79.68031231965625), "Fantastico Pizza","288 Kerr St, Oakville, ON L6K 3B3\nOpen: Mon-Sat 10 AM - 10 PM", R.drawable.fantastico_oakville)
                addPizzaMarker(LatLng(43.466404540142506, -79.74393061844337), "Pizzaville","483 Dundas St W, Oakville, ON L6M 4M2\nOpen: Mon-Sat 10 AM - 12 AM", R.drawable.pizzaville_oakville)
                addPizzaMarker(LatLng(43.43779261644522, -79.7413436225363), "Pizza Pizza","Abbey Plaza, 1500 Upper Middle Rd W, Oakville, ON L6M 3G3\nOpen: Mon-Sat 10 AM - 02 AM", R.drawable.pizza_oakville)
                addPizzaMarker(LatLng(43.488585972852334, -79.71703274872733), "Pizza Hut Oakville","380 Dundas St E Unit 4, Oakville, ON L6H 6Z9\nOpen: Mon-Sat 10 AM - 11 PM", R.drawable.pizzahut_oakville)
            }
            "North York" -> {
                // Add markers for pizza restaurants in North York
                addPizzaMarker(LatLng(43.79326131438187, -79.35371177062626), "Pizza Hut North York","3555 Don Mills Rd, North York, ON M2H 3N3\nOpen: Mon-Sat 11 AM - 11 PM", R.drawable.pizzahut_north)
                addPizzaMarker(LatLng(43.776593383720346, -79.41423809965377), "Pizza Shab","3 Byng Ave, North York, ON M2N 7H4\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.pizzashab_noth)
                addPizzaMarker(LatLng(43.764289455179984, -79.41208654024771), "Pizzaiolo","4920 Yonge St, North York, ON M2N 5N5\nOpen: Mon-Sat 11 AM - 10 PM", R.drawable.pizzaiolo_north)
                addPizzaMarker(LatLng(43.80772543236222, -79.35648374347886), "Pizza Hot Wings","3883 Don Mills Rd, North York, ON M2H 2S7\nOpen: Mon-Sat 12 PM - 10 PM", R.drawable.pizzahotwings_north)
                addPizzaMarker(LatLng(43.754154079283765, -79.3517218019068), "Tarino Pizza"," 861 York Mills Rd #2A, North York, ON M3B 1Y2\nOpen: Mon-Sat 11 AM - 12 AM", R.drawable.tarino_north)
                // Add more markers as needed
            }
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
            "Mississauga"-> LatLng(43.589045, -79.644119)
            "Oakville"-> LatLng(43.467517, -79.687666)
            "North York" -> LatLng(43.761539, -79.411079)
            // Add LatLng for other cities
            else -> null
        }
    }

    // Add a marker for a pizza restaurant
    private fun addPizzaMarker(position: LatLng, title: String, snippet:String,imageResId: Int) {
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
