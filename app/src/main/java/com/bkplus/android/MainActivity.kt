package com.bkplus.android

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
import com.bkplus.android.common.BaseActivity
import com.bkplus.android.ui.main.home.HomeViewModel
import com.bkplus.android.ui.widget.NoInternetDialogFragment
import com.bkplus.android.ui.widget.PopupExitDialogFragment
import com.bkplus.android.ultis.NetworkState
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
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
    }

    private fun setUpBottomNavigation() {
        binding.apply {
            navController?.let { bottomNav.setupWithNavController(it) }
            navController?.addOnDestinationChangedListener { _, destination, _ ->
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

    fun setVisibleBottomView(show: Boolean) {
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
                        R.id.settingFragment,
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
