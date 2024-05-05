package com.bkplus.android.ui.main.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.model.LocationSend
import com.bkplus.android.model.Trip
import com.bkplus.android.ultis.getBitmapFromVectorDrawable
import com.bkplus.android.ultis.gone
import com.bkplus.android.ultis.numberToVND
import com.bkplus.android.ultis.visible
import com.bkplus.android.websocket.WebSocket
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
import kotlinx.coroutines.Runnable
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnPolylineClickListener {

    @Inject
    lateinit var webSocket: WebSocket
    override val layoutId: Int
        get() = R.layout.fragment_map
    var googleMap: GoogleMap? = null
    private var locationSend: LocationSend? = null
    private var mGeoApiContext: GeoApiContext? = null
    private var mPolyLinesData = ArrayList<PolylineData>()
    private val mTripMarkers = ArrayList<Marker>()
    private var tripOj: Trip? = null
    private var startTrip = false

    override fun setupData() {
        super.setupData()
        val trip = arguments?.getString("trip")
        val gson = Gson()
        tripOj = gson.fromJson(trip, Trip::class.java)
        val locationMap = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationMap.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationMap.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        context?.let { context ->
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
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
                        10.0F
                    ) // Minimum distance change
                    { location ->
                        locationSend = LocationSend(
                            location.latitude,
                            location.longitude,
                            tripOj?.user?.id
                        )
                        locationSend?.let { it3 ->
                            webSocket.sendLocation(it3)
                        }


                        it2.clear()
                        googleMap = it2
                        val zoomLevel = 15f
                        val latLngDriver = LatLng(location.latitude, location.longitude)

                        it2.addMarker(
                            MarkerOptions().icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    context.getBitmapFromVectorDrawable(
                                        R.drawable.ic_current_location
                                    )
                                )
                            ).position(latLngDriver).title("Driver")
                        )
                        val cameraUpdate =
                            CameraUpdateFactory.newLatLngZoom(latLngDriver, zoomLevel)
                        it2.animateCamera(cameraUpdate)

                        val locationUser =
                            tripOj?.pick_up_location_latitude?.let { it1 ->
                                tripOj?.pick_up_location_longitude?.let { it3 ->
                                    LatLng(
                                        it1,
                                        it3
                                    )
                                }
                            }

                        locationUser?.let { latLng ->
                            it2.addMarker(
                                MarkerOptions().icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        context.getBitmapFromVectorDrawable(
                                            R.drawable.user_gps
                                        )
                                    )
                                ).position(latLng).title("User")
                            )
                        }

                        val locationDrop =
                            tripOj?.drop_off_location_latitude?.let { it1 ->
                                tripOj?.drop_off_location_longitude?.let { it3 ->
                                    LatLng(
                                        it1,
                                        it3
                                    )
                                }
                            }

                        locationDrop?.let { latLng ->
                            it2.addMarker(
                                MarkerOptions().icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        context.getBitmapFromVectorDrawable(
                                            R.drawable.ic_location
                                        )
                                    )
                                ).position(latLng).title("end location")
                            )
                        }
                        if (!startTrip) {
                            calculateDirections(locationUser, latLngDriver)
                        } else {
                            startTrip()
                        }
                    }

                }

            }

            if (mGeoApiContext == null) {
                mGeoApiContext = GeoApiContext.Builder()
                    .apiKey(getString(R.string.api_key_map))
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

    override fun setupUI() {
        super.setupUI()
        binding.apply {
            activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
            isShow = true
            updateInfoTrip()
            Handler(Looper.getMainLooper()).postDelayed({
                tvScrollTo.gone()
                btnAction.visible()
            }, 2000)
        }

    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnAction.setOnClickListener {
                startTrip()
            }

            imgDown.setOnClickListener {
                isShow = false
            }

            imgUp.setOnClickListener {
                isShow = true
            }

            btnComplete.setOnClickListener {
                completeTrip()
            }
            imgPhone.setOnClickListener {
                val phoneIntent = Intent(Intent.ACTION_CALL)
                phoneIntent.data = Uri.parse("tel:${tripOj?.user?.phone}")
                context?.startActivity(phoneIntent)
            }
        }
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


    private fun calculateDirections(mDriverPosition: LatLng?, mUserPosition: LatLng?) {
        if (mDriverPosition == null || mUserPosition == null) return
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

        directions.destination(destination)
            .setCallback(object : PendingResult.Callback<DirectionsResult?> {

                override fun onResult(result: DirectionsResult?) {
                    if (result != null) {
                        Timber.tag("huanhuan").d("onResult: routes: " + result.routes[0].toString())
                        Timber.tag("huanhuan")
                            .d("onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString())
                        addPolylineToMap(result)
                    }
                }

                override fun onFailure(e: Throwable?) {
                    Log.e("huanhuan onFailure", e?.message.toString())
                }

            })
    }

    private fun addPolylineToMap(result: DirectionsResult) {
        Handler(Looper.getMainLooper()).post(Runnable {
            if (mPolyLinesData.size > 0) {
                for (polylineData in mPolyLinesData) {
                    polylineData.polyline?.remove()
                }
                mPolyLinesData.clear()
                mPolyLinesData = ArrayList()
            }
            for (route in result.routes) {
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
                mPolyLinesData.add(PolylineData(polyline, route.legs[0]))
                polyline?.let { onPolylineClick(it) }

            }
        })
    }

    override fun onPolylineClick(polyline: Polyline) {
        var index = 0
        context?.let { context ->
            for (polylineData in mPolyLinesData) {
                if (polyline.id == polylineData.polyline?.id) {
                    index++
                    polylineData.polyline?.color =
                        ContextCompat.getColor(context, R.color.primary100)
                    polylineData.polyline?.zIndex = 1F

                    polylineData.leg?.let { directionLeg ->
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

    private fun startTrip() {
        startTrip = true
        binding.btnAction.gone()
        val lngLngStart =
            tripOj?.pick_up_location_latitude?.let { it1 ->
                tripOj?.pick_up_location_longitude?.let { it2 ->
                    LatLng(
                        it1,
                        it2
                    )
                }
            }

        val lngLngEnd =
            tripOj?.drop_off_location_latitude?.let { it1 ->
                tripOj?.drop_off_location_longitude?.let { it2 ->
                    LatLng(
                        it1,
                        it2
                    )
                }
            }
        calculateDirections(lngLngStart, lngLngEnd)
        Handler(Looper.getMainLooper()).postDelayed( {
            binding.btnComplete.visible()
        },2000)
    }

    private fun completeTrip() {
        tripOj?.let { webSocket.completeTrip(it) }
        findNavController().popBackStack()
    }

    private fun updateInfoTrip() {
        binding.apply {
            tvName.text = tripOj?.user?.name
            tvFree.text = tripOj?.fee?.let { numberToVND(it) }
            tvKm.text = String.format("%.2f Km", tripOj?.km)
            tvStartLocation.text = tripOj?.pick_up_location
            tvEndLocation.text = tripOj?.drop_off_location
            tvNote.isVisible = tripOj?.note != null
            tvNote.text = tripOj?.note
        }
    }
}