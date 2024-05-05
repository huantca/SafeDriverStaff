package com.bkplus.android.ui.main.login.loginuser

import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.Driver
import com.bkplus.android.model.User
import com.bkplus.android.ultis.Constants
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentLoginUserBinding

class LoginUserFragment : BaseFragment<FragmentLoginUserBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_login_user
    private val viewModel: SharedViewModel by activityViewModels()

    override fun setupData() {
        super.setupData()
        handlerLogin()
    }

    override fun setupListener() {
        super.setupListener()
        binding.apply {
            btnLogin.setOnClickListener {
                activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = true
                val email = edtEmail.text.trim().toString()
                if (email.substringAfter(".") == Constants.SAFE_DRIVER){
                    viewModel.login(
                        Driver(
                            email = edtEmail.text.trim().toString(),
                            password = edtPassword.text.trim().toString()
                        )
                    )
                }else{
                    viewModel.login(
                        User(
                            email = edtEmail.text.trim().toString(),
                            password = edtPassword.text.trim().toString()
                        )
                    )
                }
            }
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.registerUserFragment)
            }
        }
    }

    private fun handlerLogin() {
        viewModel.loginUserSuccessLiveData.observe(viewLifecycleOwner) {
            activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
            BasePrefers.getPrefsInstance().newLogin = true
            BasePrefers.getPrefsInstance().infoUser = it
            BasePrefers.getPrefsInstance().infoDriver = null
            findNavController().navigate(R.id.userFragment)
        }

        viewModel.loginDriverSuccessLiveData.observe(viewLifecycleOwner) {
            activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
            BasePrefers.getPrefsInstance().newLogin = true
            BasePrefers.getPrefsInstance().infoDriver = it
            BasePrefers.getPrefsInstance().infoUser = null
            findNavController().navigate(R.id.driverFragment)
        }
        viewModel.loginFailLiveData.observe(viewLifecycleOwner) {
            activity?.findViewById<FrameLayout>(R.id.loading_main)?.isVisible = false
            toast(it)
        }
    }
}