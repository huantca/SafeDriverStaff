package com.bkplus.android.ui.main.user.info

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentInfoUserBinding

class InfoUserFragment : BaseFragment<FragmentInfoUserBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_info_user

    override fun setupData() {
        super.setupData()
        val dataType = resources.getStringArray(R.array.typeCar)
        val dataCar = resources.getStringArray(R.array.car)
        val adapterType = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, dataType
            )
        }
        val adapterCar = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, dataCar
            )
        }
        binding.spinnerType.adapter = adapterType
        binding.spinnerCar.adapter = adapterCar

        binding.spinnerType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                BasePrefers.getPrefsInstance().rangeOfVehicleUser = dataType[position]
                binding.tvTypeVehicle.text = BasePrefers.getPrefsInstance().rangeOfVehicleUser
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.spinnerCar.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                BasePrefers.getPrefsInstance().vehicleModelUser = dataCar[position]
                binding.tvNameCar.text = BasePrefers.getPrefsInstance().vehicleModelUser
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    override fun setupUI() {
        super.setupUI()
        binding.apply {
            binding.tvName.text = BasePrefers.getPrefsInstance().infoUser?.name
            binding.tvPhone.text = BasePrefers.getPrefsInstance().infoUser?.phone

            BasePrefers.getPrefsInstance().vehicleModelUser?.let {
                binding.tvNameCar.text = it
            }
            BasePrefers.getPrefsInstance().rangeOfVehicleUser?.let {
                binding.tvTypeVehicle.text = it
            }
        }
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            icBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}