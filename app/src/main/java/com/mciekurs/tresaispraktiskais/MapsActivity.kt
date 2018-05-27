package com.mciekurs.tresaispraktiskais

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var latLng = LatLng(lat, long)

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //pārvalda atrašanās vietu
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //Izveido permsion sarakstu
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        // Padod izveidotus permisions, ja tie nav atļauti
        if (!hasPermissions(this, *permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 1)
            return
        }

        /** Iespējam vajag ielikt pareizā permision check ciklā */
        //pārbauda, vai network provider ir ieslēgts, savādāk error izmet
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0.0f, locationListener)
            //pārbauda, vai GPS provider ir ieslēgts
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0.0f, locationListener)
        }
    }

    private val locationListener: LocationListener = object : LocationListener{
        @SuppressLint("MissingPermission")
        override fun onLocationChanged(location: Location) {
            lat = location.latitude
            long = location.longitude

            //saglabā kā koordinātes
            latLng = LatLng(lat, long)

            //pārvieotjas uz atrašanās vietu
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.2f))

            /** Var izmantot arī ' mMap.addMarker' */
            //mMap.addMarker(MarkerOptions().position(latLng))
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //iezīmē galvenās ielas
        mMap.isTrafficEnabled = true
        //parāda tuvākāsās vietas
        mMap.isBuildingsEnabled = true
        //parāda current location
        mMap.isMyLocationEnabled = true

    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu_github, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.github -> {
                startActivity(Intent(this, GithubActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
