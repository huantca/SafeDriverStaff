package com.bkplus.android.ui.main.user

import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.Trip
import com.bkplus.android.ui.main.user.adapter.UserAdapter
import com.bkplus.android.ultis.setOnSingleClickListener
import com.bkplus.android.websocket.WebSocket
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding>() {

    @Inject
    lateinit var webSocket: WebSocket

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var trip: Trip? = null
    private var selectedPosition = 0
    private var userAdapter : UserAdapter?= null

    override val layoutId: Int
        get() = R.layout.fragment_user


    override fun setupData() {
        super.setupData()
        if (!webSocket.checkConnected()){
            webSocket.connectWebSocket()
        }
        trip = Trip()
        trip?.user = BasePrefers.getPrefsInstance().infoUser
        autocompleteForPlaces()
        trip?.user?.let {
            sharedViewModel.getListHistory(it)
        }
        userAdapter = UserAdapter()
        binding.rcyHistory.adapter = userAdapter
        sharedViewModel.historyUserLiveData.observe(viewLifecycleOwner){
            userAdapter?.updateItems(it)
        }
    }

    override fun setupUI() {
        super.setupUI()
        activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
        binding.apply {
            isHourly = false
            isShowMap = false
            tvName.text = trip?.user?.name
        }
    }
    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnHourly.setOnSingleClickListener{
                isHourly = true
            }
            btnByKm.setOnSingleClickListener{
                isHourly = false
            }
            rltStart.setOnSingleClickListener {
                selectedPosition = 0
                isShowMap = true
            }

            rltEnd.setOnSingleClickListener {
                selectedPosition = 1
                isShowMap = true
            }
            tvNext.setOnSingleClickListener {
                activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = true
                BasePrefers.getPrefsInstance().requestTrip = trip
                findNavController().navigate(R.id.tripUserFragment)
            }
        }
    }

    private fun autocompleteForPlaces() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG))
        autocompleteFragment.setCountries("VN")

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                if (selectedPosition == 0) {
                    binding.tvStart.text = place.address
                    trip?.pick_up_location = place.address
                    trip?.pick_up_location_latitude = place.latLng?.latitude
                    trip?.pick_up_location_longitude = place.latLng?.longitude
                } else {
                    binding.tvEnd.text = place.address
                    trip?.drop_off_location = place.address
                    trip?.drop_off_location_latitude = place.latLng?.latitude
                    trip?.drop_off_location_longitude = place.latLng?.longitude
                }
                binding.isShowMap = false
                checkNextAction()
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("huanhuan", "An error occurred: $status")
                binding.isShowMap = false
                context?.getString(R.string.error)?.let { toast(it) }
                checkNextAction()
            }
        })
    }
    private fun checkNextAction(){
        if (trip?.pick_up_location_latitude != null && trip?.drop_off_location_latitude != null){
            binding.tvNext.isEnabled = true
            binding.tvNext.backgroundTintList = context?.getColorStateList(R.color.primary)
        }else{
            binding.tvNext.isEnabled = false
            binding.tvNext.backgroundTintList = context?.getColorStateList(R.color.neutral50)
        }
    }
}