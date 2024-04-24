package com.bkplus.android.ui.main.driver

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.model.Driver
import com.bkplus.android.model.StatusE
import com.bkplus.android.model.Trip
import com.bkplus.android.ui.main.driver.adapter.DriverAdapter
import com.bkplus.android.ultis.observeOnce
import com.bkplus.android.websocket.WebSocket
import com.google.gson.Gson
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentDriverBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DriverFragment : BaseFragment<FragmentDriverBinding>() {

    @Inject
    lateinit var webSocket: WebSocket
    private var adapter: DriverAdapter? = null
    private var bundle: Bundle? = null
    private var location : LocationManager?= null
    private var trip : Trip?= null
    override val layoutId: Int
        get() = R.layout.fragment_driver

    override fun setupData() {
        super.setupData()
        adapter = DriverAdapter()
        bundle = Bundle()
        location = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gson = Gson()
        adapter?.action = {
            it.driver = Driver(2, name = "adam", age = 32)
            it.status = StatusE.CONFIRM
            val json = gson.toJson(it)
            bundle?.putString("trip", json)
            webSocket.acceptTrip(it)
            //findNavController().navigate(R.id.mapFragment, bundle)
            activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = true
            websocketAcceptTrip()
        }
        binding.rcyDriver.adapter = adapter
        val arrTrip = ArrayList<Trip>()
        webSocket.mutableLiveData.observe(viewLifecycleOwner) {
            it?.let { trip ->
                if (trip.status == StatusE.CANCEL) {
                    val item = arrTrip.find { tripCancel ->
                        tripCancel.id == trip.id
                    }
                    arrTrip.remove(item)
                } else {
                    arrTrip.add(trip)
                }
            }
            adapter?.updateItems(arrTrip)
        }

    }

    override fun setupUI() {
        super.setupUI()
        activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
    }

    override fun setupListener() {
        super.setupListener()

    }

    private fun websocketAcceptTrip(){
        webSocket.isDriverAcceptTripSuccess.observeOnce(viewLifecycleOwner){it2 ->
            it2?.let {boolean ->
                if (boolean){
                    findNavController().navigate(R.id.mapFragment, bundle)
                }else{
                    context?.let {
                        toast("Trip is not ready")
                    }
                }
            }
        }
    }

}