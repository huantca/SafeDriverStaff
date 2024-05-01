package com.bkplus.android.ui.main.login.loginuser

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.CountDownTimer
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.RequestOtp
import com.bkplus.android.model.User
import com.google.gson.Gson
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentVerificationLoginBinding

class OtpFragment : BaseFragment<FragmentVerificationLoginBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_verification_login
    private val viewModel: SharedViewModel by activityViewModels()
    private var user = User()
    private var timer: CountDownTimer?= null
    override fun setupData() {
        super.setupData()
        val arg = arguments?.getString("userRegister")
        val gson = Gson()
        user = gson.fromJson(arg,User::class.java)
        handlerRegister()

    }

    override fun setupUI() {
        super.setupUI()
        binding.apply {
            tvEmail.text = context?.getString(R.string.please_enter_the_6_digit_code_sent,user.email)
        }
        startCountDownTimer()

    }
    override fun setupListener() {
        super.setupListener()
        binding.apply {
            icBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnVerification.setOnClickListener {
                if (edtOtp.text.toString() == user.otp){
                    viewModel.register(user)
                }else{
                    toast(getString(R.string.otp_does_not_match))
                }
            }
            tvResendCode.setOnClickListener {
                viewModel.sendOtp(RequestOtp(email = user.email))
            }
        }
    }

    private fun handlerRegister(){
        viewModel.registerUserSuccessLiveData.observe(viewLifecycleOwner){
            toast(getString(R.string.register_sccess))
            BasePrefers.getPrefsInstance().newLogin = true
            BasePrefers.getPrefsInstance().infoUser = it
            findNavController().navigate(OtpFragmentDirections.actionOtpFragmentToUserFragment())
        }
        viewModel.registerUserFailLiveData.observe(viewLifecycleOwner){
            toast(it)
        }
    }

    override fun onStop() {
        super.onStop()
        timer?.onFinish()
    }
    private fun startCountDownTimer(){
        timer = object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResendCode.isEnabled = false
                binding.tvResendCode.setTextColor(context?.let { ColorStateList.valueOf(it.getColor(R.color.naviColor1)) })
                binding.tvTime.isVisible = true
                binding.tvTime.text = millisUntilFinished.toString() + "s"
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                binding.tvResendCode.isEnabled = true
                binding.tvResendCode.setTextColor(context?.let { ColorStateList.valueOf(it.getColor(R.color.primary)) })
                binding.tvTime.isVisible = false
            }
        }
        timer?.start()
    }
}