package com.bkplus.android.ui.main.user.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.Driver
import com.bkplus.android.model.StatusE
import com.bkplus.android.model.Trip
import com.bkplus.android.ui.widget.CompleteDialog
import com.bkplus.android.ui.widget.ConfirmDialog
import com.bkplus.android.ultis.getBitmapFromVectorDrawable
import com.bkplus.android.ultis.gone
import com.bkplus.android.ultis.numberToVND
import com.bkplus.android.ultis.setOnSingleClickListener
import com.bkplus.android.ultis.visible
import com.bkplus.android.websocket.WebSocket
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.internal.PolylineEncoding
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
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
    private val viewModel: SharedViewModel by activityViewModels()
    private val args : TripUserFragmentArgs by navArgs()
    private var googleMap: GoogleMap? = null
    private var mGeoApiContext: GeoApiContext? = null
    private var trip: Trip? = null
    private var driver: Driver? = null

    private val callPhonePermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.CALL_PHONE, false) -> {
                // Precise location access granted.
            }

            else -> {

            }
        }
    }

    override fun setupData() {
        super.setupData()
        trip = args.trip
    }

    override fun setupUI() {
        super.setupUI()
        activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
        binding.isShow = true
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                                            R.drawable.ic_current_location
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
                    calculateDirections(pickStart, dropEnd, TravelMode.DRIVING)
                    handlerLocationDriver(it, context)
                    handlerAcceptTrip()
                    handlerCompleteTrip()


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
                ConfirmDialog().apply {
                    cancel = {
                        trip?.let { it1 -> webSocket.cancelTrip(it1) }
                        webSocket.disposableLocation()
                        findNavController().popBackStack()
                    }
                }.show(childFragmentManager)
            }
            btnBook.setOnClickListener {
                requestTrip()
                btnBook.gone()
                binding.btnCancel.visible()
                binding.tvWait.visible()
            }

            btnCancel.setOnClickListener {
                ConfirmDialog().apply {
                    cancel = {
                        trip?.let { it1 -> webSocket.cancelTrip(it1) }
                        webSocket.disposableLocation()
                        findNavController().popBackStack()
                    }
                }.show(childFragmentManager)
            }

            imgDown.setOnSingleClickListener {
                isShow = false
            }

            imgUp.setOnSingleClickListener {
                isShow = true
            }

            imgPhone.setOnClickListener {
                requestCallPhone {
                    val phoneIntent = Intent(Intent.ACTION_CALL)
                    phoneIntent.data = Uri.parse("tel:${trip?.driver?.phone}")
                    context?.startActivity(phoneIntent)
                }
            }

        }
    }

    private fun handlerLocationDriver(googleMap: GoogleMap, context: Context) {
        var marker: Marker? = null
        webSocket.locationDriver.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            marker?.remove()
            val latLng = it.latitude?.let { it1 -> it.longitude?.let { it2 -> LatLng(it1, it2) } }
            marker = latLng?.let { ln ->
                googleMap.addMarker(
                    MarkerOptions()
                        .icon(
                            BitmapDescriptorFactory.fromBitmap(
                                context.getBitmapFromVectorDrawable(
                                    R.drawable.user_gps
                                )
                            )
                        )
                        .position(ln)
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handlerAcceptTrip() {
        webSocket.acceptTrip.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            driver = it.driver
            binding.ctlContainer.gone()
            binding.imgUp.gone()
            binding.ctlInfoDriver.visible()
            binding.tvNameDriver.text = it.driver?.name
            binding.tvAge.text = context?.getString(R.string.age) + it.driver?.age.toString()
            binding.tvStatusDriver.text = it.status.toString()
            binding.phone.text = context?.getString(R.string.phone_number) + ": " + it.driver?.phone
            binding.tvStartLocation.text = context?.getString(R.string.start_location)+ ": " + it.pick_up_location
            binding.tvEndLocation.text = context?.getString(R.string.end_location)+ ": "  + it.drop_off_location
            binding.tvFee2.text = getString(R.string.rates) + numberToVND(it.fee)
            binding.tvKm2.text = getString(R.string.distance) +  it.km.toString() + " km"
            activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
            trip?.driver?.phone = it.driver?.phone
            webSocket.acceptTrip.value = null
        }

    }

    private fun handlerCompleteTrip() {
        webSocket.completeTrip.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            if (it.user?.id == trip?.user?.id && it.status == StatusE.COMPLETE) {
                CompleteDialog().apply {
                    action = {
                        findNavController().popBackStack(R.id.userFragment, false)
                    }
                    vote = {
                        driver?.let { it1 -> viewModel.voteDriver(it1) }
                    }
                }.show(childFragmentManager)
                webSocket.completeTrip.value = null
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


    private fun calculateDirections(
        startLocation: LatLng?,
        endLocation: LatLng?,
        travelMode: TravelMode
    ) {
        if (startLocation == null || endLocation == null) return
        val destination = com.google.maps.model.LatLng(
            startLocation.latitude,
            startLocation.longitude
        )
        val directions = DirectionsApiRequest(mGeoApiContext)
        directions.alternatives(true)
        directions.origin(
            com.google.maps.model.LatLng(
                endLocation.latitude,
                endLocation.longitude
            )
        )

        directions.mode(travelMode)
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

    @SuppressLint("SetTextI18n")
    private fun addPolylineToMap(result: DirectionsResult) {
        Handler(Looper.getMainLooper()).post(Runnable {
            var shortestRoute = result.routes.getOrNull(0)
            var minDistance = 10000000L
            for (route in result.routes) {
                val distance = route.legs[0].distance.inMeters
                if (distance < minDistance) {
                    minDistance = distance
                    shortestRoute = route
                }
            }
            val duration = shortestRoute?.legs?.get(0)?.duration?.inSeconds
            binding.tvDuration.text = getString(R.string.intend_time) + String.format("%.2f", (duration?.div(60) ?: 30))
            binding.tvNameCar.text = BasePrefers.getPrefsInstance().vehicleModelUser
            if (trip?.hourly_rental != 0) {
                binding.tvRentFor.visible()
                binding.tvRentFor.text =
                    context?.getString(R.string.rent_for) + trip?.hourly_rental + "h"
            } else {
                binding.tvRentFor.gone()
            }
            binding.tvTypeCar.text =
                context?.getString(R.string.range_car) + BasePrefers.getPrefsInstance().rangeOfVehicleUser
            binding.tvKm.text = String.format("%.2f Km", minDistance / 1000.0)
            trip?.km = minDistance / 1000.0
            binding.tvFee.text = numberToVND(
                minDistance.toDouble() * 10 * ((trip?.hourly_rental ?: 0) + 1)
            )

            trip?.fee = minDistance.toDouble() * 10
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


    private fun requestTrip() {
        trip?.let {
            it.car_name = BasePrefers.getPrefsInstance().vehicleModelUser
            it.range_of_vehicle = BasePrefers.getPrefsInstance().rangeOfVehicleUser
            it.note = binding.edtNote.text.toString()
            it.time_start = System.currentTimeMillis()
            it.date_of_hire = System.currentTimeMillis()
            webSocket.sendRequest(it)
        }
    }

    private fun requestCallPhone(action: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            action.invoke()
        } else {
            callPhonePermissionRequest.launch(
                arrayOf(
                    Manifest.permission.CALL_PHONE
                )
            )
        }

    }

}