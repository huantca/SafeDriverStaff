package com.bkplus.android.ui.main.home

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.ads.AdsContainer
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.model.Trip
import com.bkplus.android.model.User
import com.bkplus.android.ui.main.home.adapter.HomeAdapter
import com.bkplus.android.ultis.setOnSingleClickListener
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
    private var adapter: HomeAdapter? = null

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

    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            tvSendRequest.setOnSingleClickListener {
                webSocket.sendRequest(
                    Trip(
                        user = User(
                            352,
                            name = "huấn",
                            latitude = 13.029727,
                            longitude = 77.5933021
                        ),
                        pick_up_location = "10b P. Nguyễn Hiền, Bách Khoa, Hai Bà Trưng, Hà Nội, Vietnam",
                        pick_up_location_latitude = 21.0012,
                        pick_up_location_longitude = 105.8479,
                        drop_off_location = "16 Ng. 41 P. Vọng, Đồng Tâm, Hai Bà Trưng, Hà Nội, Vietnam",
                        drop_off_location_latitude = 20.9993,
                        drop_off_location_longitude = 105.8423
                    )
                )
            }
            reconnect.setOnSingleClickListener {
                webSocket.connectWebSocket()
            }
            tvCancel.setOnSingleClickListener {
                webSocket.cancelTrip(Trip(user = User(352, name = "huấn")))
            }
            tvMap.setOnSingleClickListener {
                findNavController().navigate(R.id.mapFragment)
            }
            driver.setOnSingleClickListener {
                findNavController().navigate(R.id.driverFragment)
            }
            userFragment.setOnSingleClickListener{
                findNavController().navigate(R.id.userFragment)
            }
        }
    }

}
