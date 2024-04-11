package com.bkplus.android.ui.main.user.map

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.ultis.getBitmapFromVectorDrawable
import com.bkplus.android.websocket.WebSocket
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentTripUserBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TripUserFragment : BaseFragment<FragmentTripUserBinding>() {

    @Inject
    lateinit var webSocket: WebSocket
    override val layoutId: Int
        get() = R.layout.fragment_trip_user
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var gpsPermissionRequestFromSetting: ActivityResultLauncher<Intent>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var googleMap: GoogleMap? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private var updatedCamera = false


    override fun setupUI() {
        super.setupUI()
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val mapFragment = childFragmentManager.findFragmentById(
            R.id.map_fragment_user
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { it ->
            googleMap = it
            context?.let {context ->
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@getMapAsync
                }
                if (location != null) {
                    // Center map on current location
                    webSocket.mutableLiveDataLocation.observe(viewLifecycleOwner){locationSend ->
                        it.clear()
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                        it.addMarker(
                            MarkerOptions()
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        context.getBitmapFromVectorDrawable(
                                            R.drawable.ic_clock
                                        )
                                    )
                                )
                                .position(currentLatLng))

                        val locationDriver =
                            locationSend.latitude?.let { it1 -> locationSend.longitude?.let { it2 ->
                                LatLng(it1,
                                    it2
                                )
                            } }
                        locationDriver?.let { it1 ->
                            MarkerOptions()
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        context.getBitmapFromVectorDrawable(
                                            R.drawable.ic_current_location
                                        )
                                    )
                                )
                                .position(it1)
                        }?.let { it2 -> it.addMarker(it2) }
                    }
                } else {
                    // Handle location unavailable case (e.g., show message to user)
                }
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
}