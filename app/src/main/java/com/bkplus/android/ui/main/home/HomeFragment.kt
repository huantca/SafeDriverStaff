package com.bkplus.android.ui.main.home

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.model.Driver
import com.bkplus.android.model.StatusE
import com.bkplus.android.model.Trip
import com.bkplus.android.model.User
import com.bkplus.android.ui.main.home.adapter.HomeAdapter
import com.bkplus.android.ultis.setOnSingleClickListener
import com.bkplus.android.ultis.visible
import com.bkplus.android.websocket.WebSocket
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var adsContainer: AdsContainer

    @Inject
    lateinit var webSocket: WebSocket
    override val layoutId: Int
        get() = R.layout.fragment_home
    private val viewModel: HomeViewModel by activityViewModels()
    private var adapter : HomeAdapter?= null

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun setupUI() {
        super.setupUI()


    }

    override fun setupData() {
        super.setupData()
        webSocket.connectWebSocket()
        adapter = HomeAdapter()
        adapter?.action = {
            it.driver = Driver(2, name = "adam")
            it.status = StatusE.CONFIRM
            webSocket.acceptTrip(it)
        }
        binding.rcyTest.adapter = adapter
        val arrTrip = ArrayList<Trip>()
        webSocket.mutableLiveData.observe(viewLifecycleOwner){
            it?.let { trip ->
                binding.ctlUser.visible()
                binding.tvName.text = trip.user?.name
                binding.tvStatus.text = trip.status?.name
                if (trip.status == StatusE.CANCEL){
                    val item = arrTrip.find { tripCancel ->
                        tripCancel.id == trip.id
                    }
                    arrTrip.remove(item)
                }else{
                    arrTrip.add(trip)
                }
            }
            adapter?.updateItems(arrTrip)
        }
        webSocket.acceptTrip.observe(viewLifecycleOwner){
            binding.tvStatus.text = it.status?.name
        }
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            tvSendRequest.setOnSingleClickListener{
                webSocket.sendRequest(Trip(user = User(352, name = "huấn")))
            }
            reconnect.setOnSingleClickListener{
                webSocket.connectWebSocket()
            }
            tvCancel.setOnSingleClickListener{
                webSocket.cancelTrip(Trip(user = User(352, name = "huấn")))
            }
            tvMap.setOnSingleClickListener{
                findNavController().navigate(R.id.mapFragment)
            }
        }
    }

}
