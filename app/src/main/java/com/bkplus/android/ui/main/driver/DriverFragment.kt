package com.bkplus.android.ui.main.driver

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.MainActivity
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.StatusE
import com.bkplus.android.model.Trip
import com.bkplus.android.ui.main.driver.adapter.DriverAdapter
import com.bkplus.android.ui.widget.PermissionLocationDialog
import com.bkplus.android.ultis.numberToVND
import com.bkplus.android.ultis.observeOnce
import com.bkplus.android.websocket.WebSocket
import com.google.gson.Gson
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentDriverBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class DriverFragment : BaseFragment<FragmentDriverBinding>() {

    @Inject
    lateinit var webSocket: WebSocket
    private var adapter: DriverAdapter? = null
    private var bundle: Bundle? = null
    private var location: LocationManager? = null
    private val viewModel: SharedViewModel by activityViewModels()
    private var trip: Trip? = null
    override val layoutId: Int
        get() = R.layout.fragment_driver

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }

            else -> {
                PermissionLocationDialog().show(childFragmentManager)
            }
        }
    }

    override fun setupData() {
        super.setupData()
        var arrClone = ArrayList<Trip>()
        if (!webSocket.checkConnected()) {
            webSocket.connectWebSocket()
        }
        adapter = DriverAdapter()
        bundle = Bundle()
        location = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gson = Gson()
        adapter?.action = {
            it.driver = BasePrefers.getPrefsInstance().infoDriver
            it.status = StatusE.CONFIRM
            val json = gson.toJson(it)
            bundle?.putString("trip", json)

            //webSocket.mutableLiveData.postValue(arrClone.removeAt(it.id))
            webSocket.acceptTrip(it)
            activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = true
            websocketAcceptTrip()
        }
        binding.rcyDriver.adapter = adapter
        webSocket.mutableLiveData.observe(viewLifecycleOwner) {
//            it?.let { trip ->
//                if (trip.status == StatusE.CANCEL) {
//                    val item = arrTrip.find { tripCancel ->
//                        tripCancel.id == trip.id
//                    }
//                    arrTrip.remove(item)
//                } else {
//                    arrTrip.add(trip)
//                }
//            }
            arrClone = ArrayList(it.map { it.copy() })
            adapter?.updateItems(arrClone)
        }

        handlerHistoryDriver()
        requestLocation()
        BasePrefers.getPrefsInstance().infoDriver?.let {
            viewModel.getListHistoryDriver(it)
        }
    }

    override fun setupUI() {
        super.setupUI()
        activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
        binding.tvName.text = BasePrefers.getPrefsInstance().infoDriver?.name
        BasePrefers.getPrefsInstance().infoDriver?.star_number?.let {
            binding.tvStar.text = it.toString()
        }
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            icLogout.setOnClickListener {
                BasePrefers.getPrefsInstance().newLogin = false
                activity?.finish()
                activity?.startActivity(Intent(context, MainActivity::class.java))
            }
            imgHistory.setOnClickListener {
                findNavController().navigate(R.id.historyDriver)
            }
        }
    }

    private fun websocketAcceptTrip() {
        webSocket.isDriverAcceptTripSuccess.observeOnce(viewLifecycleOwner) { it2 ->
            it2?.let { boolean ->
                if (boolean) {
                    findNavController().navigate(R.id.mapFragment, bundle)
                } else {
                    context?.let {
                        toast("Trip is not ready")
                    }
                }
            }
        }
    }

    private fun handlerHistoryDriver() {
        viewModel.historyDriverLiveData.observe(viewLifecycleOwner) {
            var count: Int = 0;
            var money: Double = 0.0;
            it.forEach { trip ->
                trip.date_of_hire?.let { time ->
                    if (Date(System.currentTimeMillis()).day - Date(time).day == 0
                        && Date(System.currentTimeMillis()).month - Date(time).month == 0
                        && Date(System.currentTimeMillis()).year - Date(time).year == 0
                    ) {
                        count++
                        trip.fee?.let { fee ->
                            money += (fee * 70) / 100
                        }
                    }
                }
            }
            binding.tvCount.text = count.toString()
            binding.tvMoney.text = numberToVND(money)
        }
    }

    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

}