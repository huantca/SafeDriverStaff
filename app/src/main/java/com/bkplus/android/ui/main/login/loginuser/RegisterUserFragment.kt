package com.bkplus.android.ui.main.login.loginuser

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.model.RequestOtp
import com.bkplus.android.model.User
import com.google.gson.Gson
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentRegisterUserBinding

class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_register_user
    private val viewModel: SharedViewModel by activityViewModels()
    private var user: User? = null
    private var gson: Gson? = null
    private var bundle: Bundle? = null

    override fun setupData() {
        super.setupData()
        user = User()
        gson = Gson()
        bundle = Bundle()
        handlerSendOtp()
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            tvLogin.setOnClickListener {
                findNavController().popBackStack()
            }
            btnRegister.setOnClickListener {
                viewModel.sendOtp(RequestOtp(email = edtEmail.text.trim().toString()))
            }
        }
    }

    private fun handlerSendOtp(){
        viewModel.sendOtpUserSuccessLiveData.observe(viewLifecycleOwner){
            user?.name = binding.edtName.text.toString()
            user?.email = binding.edtEmail.text.toString()
            user?.phone = binding.edtPhone.text.toString()
            user?.password = binding.edtPassword.text.toString()
            user?.otp = it
            val json = gson?.toJson(user)
            bundle?.putString("userRegister", json)

            findNavController().navigate(
                R.id.otpFragment, bundle
            )
        }
        viewModel.sendOtpUserFailLiveData.observe(viewLifecycleOwner){
            toast(it)
        }
    }
}