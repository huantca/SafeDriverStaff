package com.bkplus.android.ui.main.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override val layoutId: Int
        get() = R.layout.fragment_user

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                requestLocation()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                requestLocation()
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

        //tab
        val tab1: TabLayout.Tab = binding.tablayout.newTab()
        tab1.text = getString(R.string.by_km)
        val tab2: TabLayout.Tab = binding.tablayout.newTab()
        tab2.text = getString(R.string.hourly)
        binding.tablayout.addTab(tab1)
        binding.tablayout.addTab(tab2)
        val textview1 = LayoutInflater.from(context).inflate(R.layout.tab_title,null) as TextView
        textview1.text = getString(R.string.by_km)
        val textview2 = LayoutInflater.from(context).inflate(R.layout.tab_title,null) as TextView
        textview2.text = getString(R.string.hourly)
        binding.tablayout.getTabAt(0)?.customView = textview1
        binding.tablayout.getTabAt(1)?.customView = textview2

        binding.tablayout.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.isHourly = tab?.position != 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        //
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

        val itemsHour = arrayListOf(1, 2, 3, 4, 5)
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
                    findNavController().navigate(
                        UserFragmentDirections.actionUserFragmentToTripUserFragment(
                            trip = trip
                        )
                    )
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
        ) {
            activity?.let {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            trip?.pick_up_location_longitude = longitude
                            trip?.pick_up_location_latitude = latitude
                        }
                    }
            }
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}