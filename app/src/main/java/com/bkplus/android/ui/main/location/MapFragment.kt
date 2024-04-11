package com.bkplus.android.ui.main.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.model.LocationSend
import com.bkplus.android.model.Trip
import com.bkplus.android.websocket.WebSocket
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(),OnPolylineClickListener {

    @Inject
    lateinit var webSocket: WebSocket

    private val TAG = "huan"
    override val layoutId: Int
        get() = R.layout.fragment_map
    var latLng: LatLng? = null
    var googleMap: GoogleMap? = null
    private var locationSend: LocationSend? = null
    private var mGeoApiContext: GeoApiContext? = null
    private var mPolyLinesData = ArrayList<PolylineData>()
    private val mTripMarkers = ArrayList<Marker>()

    override fun setupData() {
        super.setupData()
        val trip = arguments?.getString("trip")
        val gson = Gson()
        val tripOj = gson.fromJson(trip, Trip::class.java)
        val locationMap = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationMap.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationMap.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

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
            } else {
                val mapFragment = childFragmentManager.findFragmentById(
                    R.id.map_fragment
                ) as? SupportMapFragment
                mapFragment?.getMapAsync { it2 ->
                    it2.setOnPolylineClickListener(this)
                    locationMap.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000L, // Minimum time between updates
                        10.0F, // Minimum distance change
                        object : LocationListener {
                            override fun onLocationChanged(location: Location) {
                                locationSend = LocationSend(
                                    location.latitude,
                                    location.longitude,
                                    tripOj.user?.id
                                )
                                locationSend?.let { it3 ->
                                    webSocket.sendLocation(it3)
                                }


                                it2.clear()
                                googleMap = it2
                                val zoomLevel = 15f
                                val lc = LatLng(location.latitude, location.longitude)
                                addMarkers(it2, lc)
                                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(lc, zoomLevel)
                                it2.animateCamera(cameraUpdate)

                                val locationUser =
                                    tripOj.pick_up_location_latitude?.let { it1 ->
                                        tripOj.pick_up_location_longitude?.let { it3 ->
                                            LatLng(
                                                it1,
                                                it3
                                            )
                                        }
                                    }

                                locationUser?.let { latLng ->
                                    it2.addMarker(MarkerOptions().position(latLng).title("User"))
                                    calculateDirections(latLng,lc)
                                }
                            }

                        }
                    )

                }

            }

            if (mGeoApiContext == null) {
                mGeoApiContext = GeoApiContext.Builder()
                    .apiKey("AIzaSyCqpHHNZ1jLfRMSO5mpDYn0pfsR96U3gi8")
                    .build()
            }

        }
    }

    private fun addMarkers(googleMap: GoogleMap, latLng: LatLng) {
        googleMap.addMarker(
            MarkerOptions()
                .title("huan")
                .position(latLng)
        )
    }

//    private fun resetSelectedMarker() {
//        if (mSelectedMarker != null) {
//            mSelectedMarker.setVisible(true)
//            mSelectedMarker = null
//            removeTripMarkers()
//        }
//    }

    private fun removeTripMarkers() {
        for (marker in mTripMarkers) {
            marker.remove()
        }
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


    private fun calculateDirections(mDriverPosition: LatLng,mUserPosition: LatLng) {
        val destination = com.google.maps.model.LatLng(
            mDriverPosition.latitude,
            mDriverPosition.longitude
        )
        val directions = DirectionsApiRequest(mGeoApiContext)
        directions.alternatives(true)
        directions.origin(
            com.google.maps.model.LatLng(
                mUserPosition.latitude,
                mUserPosition.longitude
            )
        )

        directions.destination(destination).setCallback(object : PendingResult.Callback<DirectionsResult?> {

            override fun onResult(result: DirectionsResult?) {
                if (result != null) {
                    Timber.tag("huanhuan").d("onResult: routes: " + result.routes[0].toString())
                    Timber.tag("huanhuan")
                        .d("onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString())
                    addPolylinesToMap(result)
                }
            }

            override fun onFailure(e: Throwable?) {

            }

        })
    }

    private fun addPolylinesToMap(result: DirectionsResult) {
        Handler(Looper.getMainLooper()).post(Runnable {
            Log.d(TAG, "run: result routes: " + result.routes.size)

            if (mPolyLinesData.size > 0) {
                for (polylineData in mPolyLinesData) {
                    polylineData.polyline?.remove()
                }
                mPolyLinesData.clear()
                mPolyLinesData = ArrayList()
            }
            for (route in result.routes) {
                Log.d(TAG, "run: leg: " + route.legs[0].toString())
                val decodedPath = PolylineEncoding.decode(route.overviewPolyline.encodedPath)
                val newDecodedPath: MutableList<LatLng> = ArrayList()

                // This loops through all the LatLng coordinates of ONE polyline.
                for (latLng in decodedPath) {
                    newDecodedPath.add(
                        LatLng(
                            latLng.lat,
                            latLng.lng
                        )
                    )
                }
                val polyline: Polyline? =
                    googleMap?.addPolyline(PolylineOptions().addAll(newDecodedPath))
                activity?.let {
                    polyline?.color = ContextCompat.getColor(it, R.color.dark3)
                    polyline?.isClickable = true
                }
                mPolyLinesData.add(PolylineData(polyline,route.legs[0]))
                polyline?.let { onPolylineClick(it) }
                
            }
        })
    }

    override fun onPolylineClick(polyline: Polyline) {
        var index = 0
        context?.let {context ->
            for (polylineData in mPolyLinesData) {
                if (polyline.id == polylineData.polyline?.id) {
                    index++
                    polylineData.polyline?.color = ContextCompat.getColor(context, R.color.primary100)
                    polylineData.polyline?.zIndex = 1F

                    polylineData.leg?.let {directionLeg ->
                        val endLocation = LatLng(
                            directionLeg.endLocation.lat,
                            directionLeg.endLocation.lng
                        )

                        val marker: Marker? = googleMap?.addMarker(
                            MarkerOptions()
                                .position(endLocation)
                                .title("Trip #$index")
                                .snippet(
                                    "Duration: " + directionLeg.duration
                                )
                        )


                        marker?.showInfoWindow()
                        marker?.let { mTripMarkers.add(it) }
                    }

                } else {
                    polylineData.polyline?.color = ContextCompat.getColor(context, R.color.dark3)
                    polylineData.polyline?.zIndex = 0F
                }
            }
        }

    }
}