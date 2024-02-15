@file:Suppress("DEPRECATION")

package com.bkplus.callscreen.ultis

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class NetworkState constructor(applicationContext: Context) : LiveData<Boolean>() {

    companion object {
        private const val TAG = "NetworkManager"
        const val TIME_DELAY_CHECK_NETWORK = 800L
    }

    private var conManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
    private var callbackRunnable: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())

    init {
        postNetworkCallback()
    }

    override fun onActive() {
        super.onActive()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
            conManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        conManager.unregisterNetworkCallback(networkCallback)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postNetworkCallback()
        }

        override fun onLost(network: Network) {
            postNetworkCallback()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                postNetworkCallback()
            }
        }
    }

    private fun postNetworkCallback() {
        callbackRunnable?.let { handler.removeCallbacks(it) }
        callbackRunnable = Runnable {
            val ni = conManager.activeNetworkInfo
            val isNetworkAvailable = (ni != null) && ni.isConnected && isInternetAvailable()
            if (isNetworkAvailable) {
                postValue(true)
                Timber.d("----NetworkManager: true")
            } else {
                postValue(false)
                Timber.d("----NetworkManager: false")
            }
        }
        callbackRunnable?.let { handler.postDelayed(it, TIME_DELAY_CHECK_NETWORK) }
    }

    private fun isInternetAvailable(): Boolean {
        val networkCapabilities = conManager.activeNetwork ?: return false
        val actNw =
            conManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
