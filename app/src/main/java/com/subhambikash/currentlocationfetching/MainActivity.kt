package com.subhambikash.currentlocationfetching

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var locationText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationText = findViewById(R.id.location)


        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            resultLauncher.launch(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            loadLocation()
        }


    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            it.entries.forEach { result ->
                if (result.value) {
                    loadLocation()
                }
            }
        }

    private fun loadLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        getCityName(location)


        val locationListener = LocationListener {
            getCityName(it)
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                100,
                100.2f,
                locationListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCityName(location: Location?) {
        try {
            locationText.text="Loading...."
            val gc = Geocoder(this, Locale.getDefault())
            val address = gc.getFromLocation(location!!.latitude, location.longitude, 2)
            val cityName = address[0]
            val country = cityName.countryName
            val state = cityName.adminArea
            val city = cityName.getAddressLine(0)
            val latitude = location.latitude
            val longitude = location.longitude
            locationText.text = "city name - $city\nstate name-$state\ncountry name-$country\nlatitude-$latitude\nlongitude-$longitude"

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}