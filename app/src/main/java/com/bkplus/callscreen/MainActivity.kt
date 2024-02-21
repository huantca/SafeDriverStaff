package com.bkplus.callscreen

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bkplus.callscreen.common.BaseActivity
import com.bkplus.callscreen.ui.main.home.viewmodel.HomeViewModel
import com.bkplus.callscreen.ui.widget.NoInternetDialogFragment
import com.bkplus.callscreen.ui.widget.PopupExitDialogFragment
import com.bkplus.callscreen.ultis.NetworkState
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import timber.log.Timber
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: HomeViewModel by viewModels()
    private var navController: NavController? = null
    override val layoutId: Int
        get() = R.layout.activity_main

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                Timber.tag("----").d("Permission has been granted by user")
            }

            false -> {
                Timber.tag("----").e("Permission notification has been denied")
            }
        }
    }

    @Inject
    lateinit var networkState: NetworkState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        registerBackPress()
        setUpNoInternetDialog()
        setUpBottomNavigation()
        requestNotificationPermissionAndroid13()
        viewModel.getHomeSection()
    }

    private fun setUpBottomNavigation() {
        binding.apply {
            navController?.let { bottomNav.setupWithNavController(it) }
            navController?.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.id) {
                    R.id.splashFragment,
                    R.id.onboardFragment,
                    -> {
                        setVisibleBottomView(false)
                    }

                    R.id.homeFragment -> {
                        setVisibleBottomView(true)
                        tabLayout.getTabAt(0)?.select()
                    }

                    R.id.categoryFragment -> {
                        setVisibleBottomView(true)
                        tabLayout.getTabAt(1)?.select()
                    }

                    R.id.historyFragment -> {
                        setVisibleBottomView(true)
                        tabLayout.getTabAt(2)?.select()
                    }

                    R.id.settingFragment -> {
                        setVisibleBottomView(true)
                        tabLayout.getTabAt(3)?.select()
                    }

                    else -> {
                        setVisibleBottomView(false)
                    }
                }
            }
        }

    }

    open fun setVisibleBottomView(show: Boolean) {
        binding.apply {
            bottomNav.isVisible = show
            tabLayout.isVisible = show
        }
    }

    private fun setUpNoInternetDialog() {
        val dialog = NoInternetDialogFragment.newInstance()
        networkState.observe(this) {
            if (it.not()) {
                dialog.show(supportFragmentManager, null)
            } else {
                if (dialog.isAdded) dialog.dismissNow()
            }
        }
    }

    private fun registerBackPress() {
        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (navController?.currentDestination?.id) {
                        R.id.splashFragment,
                        R.id.onboardFragment,
                        R.id.firstLanguageFragment,
                        R.id.welcomeFragment,
                        R.id.historyFragment,
                        R.id.homeFragment
                        -> {
                            PopupExitDialogFragment().apply {
                                actionYes = {
                                    exitProcess(0)
                                }
                            }.show(supportFragmentManager, "")
                        }

                        else -> {
                            navController?.popBackStack()
                        }
                    }
                }
            })
    }

    private fun requestNotificationPermissionAndroid13() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PermissionChecker.PERMISSION_DENIED &&
                !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    fun showLoading() {
        binding.loadingMain.isVisible = true
    }

    fun hideLoading() {
        binding.loadingMain.isVisible = false
    }

}
