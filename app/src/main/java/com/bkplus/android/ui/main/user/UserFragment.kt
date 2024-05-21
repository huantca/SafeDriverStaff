package com.bkplus.android.ui.main.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.bkplus.android.model.Trip
import com.bkplus.android.ui.main.user.adapter.UserAdapter
import com.bkplus.android.ui.widget.PermissionLocationDialog
import com.bkplus.android.ui.widget.SelectCarDialog
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
    private var userAdapter: UserAdapter? = null

    override val layoutId: Int
        get() = R.layout.fragment_user

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
        requestLocation()
        if (!webSocket.checkConnected()) {
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

        val hour = resources.getStringArray(R.array.hour)
        val adapterHour = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item, hour
            )
        }

        val itemsHour = arrayListOf(1,2,3,4,5)
        binding.spinnerHour.adapter = adapterHour
        binding.spinnerHour.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                trip?.hourly_rental = itemsHour[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        sharedViewModel.historyUserLiveData.observe(viewLifecycleOwner) {
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
            btnHourly.setOnSingleClickListener {
                isHourly = true
                btnHourly.setBackgroundResource(R.drawable.bg_radius_8_selected_home)
                btnByKm.setBackgroundResource(R.drawable.bg_radius_8_unselected_home)
                context?.getColor(R.color.c100D40)?.let { it1 -> btnHourly.setTextColor(it1) }
                context?.getColor(R.color.c686767)?.let { it1 -> btnByKm.setTextColor(it1) }
            }
            btnByKm.setOnSingleClickListener {
                isHourly = false
                btnByKm.setBackgroundResource(R.drawable.bg_radius_8_selected_home)
                btnHourly.setBackgroundResource(R.drawable.bg_radius_8_unselected_home)
                context?.getColor(R.color.c100D40)?.let { it1 -> btnByKm.setTextColor(it1) }
                context?.getColor(R.color.c686767)?.let { it1 -> btnHourly.setTextColor(it1) }
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
                if (BasePrefers.getPrefsInstance().vehicleModelUser == null || BasePrefers.getPrefsInstance().rangeOfVehicleUser == null) {
                    activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
                    SelectCarDialog().apply {
                        action = {
                            findNavController().navigate(R.id.infoUserFragment)
                        }
                    }.show(childFragmentManager)
                } else {
                    if (binding.isHourly == false) trip?.hourly_rental = 0
                    findNavController().navigate(UserFragmentDirections.actionUserFragmentToTripUserFragment(
                        trip = trip
                    ))
                }

            }

            icLogout.setOnClickListener {
                BasePrefers.getPrefsInstance().newLogin = false
                activity?.finish()
                activity?.startActivity(Intent(context, MainActivity::class.java))
            }

            imgAvatar.setOnClickListener {
                findNavController().navigate(R.id.infoUserFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkNextAction()
    }
    private fun autocompleteForPlaces() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )
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

    private fun checkNextAction() {
        if (trip?.pick_up_location_latitude != null && trip?.drop_off_location_latitude != null) {
            binding.tvNext.isEnabled = true
            binding.tvNext.backgroundTintList = context?.getColorStateList(R.color.primary)
        } else {
            binding.tvNext.isEnabled = false
            binding.tvNext.backgroundTintList = context?.getColorStateList(R.color.neutral50)
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