package com.bkplus.callscreen

import androidx.activity.viewModels
import com.bkplus.callscreen.common.BaseActivity
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ActivityScreenCallBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityScreenCallBinding>() {
    private val viewModel: SharedViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.activity_screen_call

}