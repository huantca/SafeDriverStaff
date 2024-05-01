package com.bkplus.android.ui.main.login.loginuser

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.User
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
                viewModel.login(
                    User(
                        email = edtEmail.text.trim().toString(),
                        password = edtPassword.text.trim().toString()
                    )
                )
            }
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.registerUserFragment)
            }
        }
    }

    private fun handlerLogin(){
        viewModel.loginUserSuccessLiveData.observe(viewLifecycleOwner){
            BasePrefers.getPrefsInstance().newLogin = true
            BasePrefers.getPrefsInstance().infoUser = it
            findNavController().navigate(R.id.userFragment)
        }
        viewModel.loginUserFailLiveData.observe(viewLifecycleOwner){
            toast(it)
        }
    }
}