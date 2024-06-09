package com.bkplus.android.ui.main.login.loginuser

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.model.RequestOtp
import com.bkplus.android.model.User
import com.bkplus.android.ultis.gone
import com.bkplus.android.ultis.visible
import com.google.gson.Gson
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentRegisterUserBinding
import java.util.regex.Pattern

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
                if (binding.edtName.text.toString() == "" || binding.edtEmail.text.toString() == "" || binding.edtPhone.text.toString() == ""
                    || binding.edtPassword.text.toString() == ""){
                    toast(getString(R.string.missing_data))
                    return@setOnClickListener
                }
                if (isValidPassword(binding.edtPassword.text.toString())) {
                    binding.tvValidatePass.gone()
                    activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = true
                    viewModel.sendOtp(RequestOtp(email = edtEmail.text.trim().toString()))
                } else {
                    binding.tvValidatePass.visible()
                }
            }
            imgEye.setOnClickListener {
                binding.edtPassword.inputType = EditorInfo.TYPE_CLASS_TEXT
                binding.edtPassword.transformationMethod = null
                binding.imgEye.gone()
                binding.imgEyeGone.visible()
            }
            imgEyeGone.setOnClickListener {
                binding.edtPassword.inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.imgEye.visible()
                binding.imgEyeGone.gone()
            }

        }
    }

    private fun handlerSendOtp() {
        viewModel.sendOtpUserSuccessLiveData.observe(viewLifecycleOwner) {
            user?.name = binding.edtName.text.toString()
            user?.email = binding.edtEmail.text.toString()
            user?.phone = binding.edtPhone.text.toString()
            user?.password = binding.edtPassword.text.toString()
            user?.otp = it
            val json = gson?.toJson(user)
            bundle?.putString("userRegister", json)
            activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
            findNavController().navigate(
                R.id.otpFragment, bundle
            )
        }
        viewModel.sendOtpUserFailLiveData.observe(viewLifecycleOwner) {
            toast(it)
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val lowercase: Pattern = Pattern.compile("[a-z]")
        val digit: Pattern = Pattern.compile("[0-9]")
        return lowercase.matcher(password).find() && digit.matcher(password).find() && password.length > 4
    }
}