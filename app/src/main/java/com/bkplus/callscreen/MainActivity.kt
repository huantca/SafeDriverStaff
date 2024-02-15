package com.bkplus.callscreen

import androidx.activity.viewModels
import com.bkplus.callscreen.common.BaseActivity
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: SharedViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.activity_main

}