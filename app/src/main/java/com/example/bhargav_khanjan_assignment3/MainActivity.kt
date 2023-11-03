// Team: Bhargav Borse - 301278352 and Khanjan Dave - 301307330
// Assignment 3
// File: MainActivity.kt
// Description: This Android app simplifies the search for pizza restaurants. Users can choose from a list of cities, including Toronto, Scarborough, Mississauga, Oakville, and North York, and a few personal favorites from different regions. The app then provides location and address details for pizza restaurants in the selected city, making it easy to find and enjoy delicious pizza wherever you are.
// Bhargav - design the UI and send selected city to next page using shared preference
package com.example.bhargav_khanjan_assignment3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Define a click listener for city buttons
        val cityClickListener = View.OnClickListener { view ->
            val city = when (view.id) {
                R.id.torontoButton -> "Toronto"
                R.id.scarboroughButton -> "Scarborough"
                R.id.mississaugaButton -> "Mississauga"
                R.id.oakvilleButton -> "Oakville"
                R.id.northyorkButton -> "North York"
                else -> "Toronto"
            }
            // Save the selected city to SharedPreferences
            sharedPreferences.edit().putString("selectedCity", city).apply()

            // Start the CityDetailsActivity
            val intent = Intent(this, MapResultActivity::class.java)
            startActivity(intent)
        }

        // Set the click listeners for city buttons
        findViewById<View>(R.id.torontoButton).setOnClickListener(cityClickListener)
        findViewById<View>(R.id.scarboroughButton).setOnClickListener(cityClickListener)
        findViewById<View>(R.id.mississaugaButton).setOnClickListener(cityClickListener)
        findViewById<View>(R.id.oakvilleButton).setOnClickListener(cityClickListener)
        findViewById<View>(R.id.northyorkButton).setOnClickListener(cityClickListener)
    }
}