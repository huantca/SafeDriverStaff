package com.bkplus.android.ui.main.user.map

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.Trip
import com.bkplus.android.ultis.getBitmapFromVectorDrawable
import com.bkplus.android.ultis.setOnSingleClickListener
import com.bkplus.android.websocket.WebSocket
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentTripUserBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TripUserFragment : BaseFragment<FragmentTripUserBinding>() {

    @Inject
    lateinit var webSocket: WebSocket
    override val layoutId: Int
        get() = R.layout.fragment_trip_user
    private lateinit var gpsPermissionRequestFromSetting: ActivityResultLauncher<Intent>
    private var googleMap: GoogleMap? = null
    private var mGeoApiContext: GeoApiContext? = null
    private var trip: Trip? = null

    override fun setupData() {
        super.setupData()
        trip = BasePrefers.getPrefsInstance().requestTrip
    }

    override fun setupUI() {
        super.setupUI()
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment_user
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { it ->
            googleMap = it
            context?.let { context ->
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@let
                }
                if (location != null) {
                    // Center map on current location
                    it.clear()
                    val pickStart =
                        trip?.pick_up_location_latitude?.let { it1 ->
                            trip?.pick_up_location_longitude?.let { it2 ->
                                LatLng(
                                    it1,
                                    it2
                                )
                            }
                        }
                    pickStart?.let { latLng ->
                        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        it.addMarker(
                            MarkerOptions()
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        context.getBitmapFromVectorDrawable(
                                            R.drawable.user_gps
                                        )
                                    )
                                )
                                .position(latLng)
                        )
                    }


                    val dropEnd =
                        trip?.drop_off_location_latitude?.let { it1 ->
                            trip?.drop_off_location_longitude?.let { it2 ->
                                LatLng(
                                    it1,
                                    it2
                                )
                            }
                        }
                    dropEnd?.let { it1 ->
                        MarkerOptions()
                            .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    context.getBitmapFromVectorDrawable(
                                        R.drawable.ic_location
                                    )
                                )
                            )
                            .position(it1)
                    }?.let { it2 -> it.addMarker(it2) }

                    calculateDirections(pickStart, dropEnd)
                }

            }
        }

        if (mGeoApiContext == null) {
            mGeoApiContext = GeoApiContext.Builder()
                .apiKey(getString(R.string.api_key_map))
                .build()
        }

    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            imgBack.setOnSingleClickListener {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }

    private fun statusCheck(action: () -> Unit) {
        val manager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {
            buildAlertMessageNoGps()
        } else {
            action.invoke()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(getString(R.string.your_GPS))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes),
                DialogInterface.OnClickListener { _, _ ->
                    gpsPermissionRequestFromSetting.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                })
            .setNegativeButton(getString(R.string.no),
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })
        val alert: AlertDialog = builder.create()
        alert.show()
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
            var shortestRoute = result.routes.getOrNull(0)
            var minDistance = 1000L
            for (route in result.routes) {
                val distance = route.legs[0].distance.inMeters
                if (distance < minDistance) {
                    minDistance = distance
                    shortestRoute = route
                }
            }
            binding.tvKm.text = (minDistance / 1000.1f).toString()
            binding.tvFee.text = (minDistance * 10).toString()
            val decodedPath = PolylineEncoding.decode(shortestRoute?.overviewPolyline?.encodedPath)
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
                polyline?.color = ContextCompat.getColor(it, R.color.primary)
                polyline?.isClickable = true
            }
        })
    }

}