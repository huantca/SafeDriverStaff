package com.bkplus.android.ui.main.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.bkplus.android.common.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentMapBinding

class MapFragment : BaseFragment<FragmentMapBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_map
    var latLng : LatLng?= null

    override fun setupData() {
        super.setupData()

        val location = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = location.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = location.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        context?.let {
            if (ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions()
                return
            }else{
                location.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, // or NETWORK_PROVIDER
                    1000L, // Minimum time between updates
                    10.0F, // Minimum distance change
                    object : LocationListener{
                        override fun onLocationChanged(location: Location) {
                            latLng = LatLng(location.latitude,location.longitude)
                        }

                    }
                )
                Log.e("huanhuan","location")
                val mapFragment = childFragmentManager.findFragmentById(
                    R.id.map_fragment
                ) as? SupportMapFragment
                mapFragment?.getMapAsync { googleMap ->
                    latLng?.let { it1 -> addMarkers(googleMap, it1) }
                }
            }

        }
    }

    private fun addMarkers(googleMap: GoogleMap,latLng: LatLng) {
        googleMap.addMarker(
            MarkerOptions()
                .title("huan")
                .position(latLng)
        )
    }

    private fun requestPermissions() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                0
            )
        }
    }
}