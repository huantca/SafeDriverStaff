package com.bkplus.android.websocket

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.LocationSend
import com.bkplus.android.model.Trip
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocket @Inject constructor() {

    private var mStompClient: StompClient? = null

    val mutableLiveData = MutableLiveData<ArrayList<Trip>>()
    val acceptTrip = MutableLiveData<Trip>()
    val locationDriver = MutableLiveData<LocationSend>()
    val completeTrip = MutableLiveData<Trip>()
    val isDriverAcceptTripSuccess = MutableLiveData<Boolean>()
    private var disposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun connectWebSocket() {

        mStompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "wss://humble-topical-krill.ngrok-free.app/websocket"
        )
        mStompClient?.lifecycle()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(Schedulers.io())
            ?.subscribe(
                { lifeCycleEvent ->
                    when (lifeCycleEvent.type) {
                        LifecycleEvent.Type.OPENED ->{
                            subAll(mStompClient)
                        }

                        LifecycleEvent.Type.CLOSED -> {
                            mStompClient?.connect()
                        }

                        LifecycleEvent.Type.ERROR -> Log.e(
                            this.javaClass.simpleName,
                            "error: ${lifeCycleEvent.exception.message}"
                        )

                        LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> Log.e(
                            this.javaClass.simpleName,
                            "failed server heartbeat"
                        )

                        null -> Unit
                    }
                },
                {
                    Log.e(this.javaClass.simpleName, "error: ${it.message}")
                }
            )
        mStompClient?.connect()
    }

    private fun subAll(mStompClient: StompClient?) {
        val arrTrip = ArrayList<Trip>()
        val gson = Gson()
        disposable.dispose()
        disposable = CompositeDisposable()
        disposable.addAll(
            mStompClient?.topic("/topic/trip/" + BasePrefers.getPrefsInstance().infoDriver?.id)
                ?.subscribe(
                    { topicMessage: StompMessage ->
                        val json = gson.fromJson(topicMessage.payload, Trip::class.java)
                        arrTrip.add(json)
                        mutableLiveData.postValue(arrTrip)
                    }, {
                        Timber.tag("WebSocket").e(it.printStackTrace().toString())
                    }
                ),
            mStompClient?.topic("/topic/cancel/" + BasePrefers.getPrefsInstance().infoDriver?.id)
                ?.subscribe(
                    { topicMessage: StompMessage ->
                        val json = gson.fromJson(topicMessage.payload, Trip::class.java)
                        arrTrip.remove(json)
                        mutableLiveData.postValue(arrTrip)
                        Timber.tag("mutableLiveData").d(topicMessage.payload)
                    }, {
                        Timber.tag("WebSocket").e(it.printStackTrace().toString())
                    }
                ),
            mStompClient?.topic("/topic/driver/" + BasePrefers.getPrefsInstance().infoUser?.id)
                ?.subscribe(
                    { topicMessage: StompMessage ->
                        val json = gson.fromJson(topicMessage.payload, Trip::class.java)
                        acceptTrip.postValue(json)
                        Timber.tag("huan driver accepted").d(topicMessage.payload)
                    }, {
                        Timber.tag("huan driver no accepted").d(BasePrefers.getPrefsInstance().infoUser?.id.toString())
                        Timber.tag("WebSocket").e(it.printStackTrace().toString())
                    }
                ),
            mStompClient?.topic("/topic/completeTrip/" + BasePrefers.getPrefsInstance().infoUser?.id)
                ?.subscribe(
                    { topicMessage: StompMessage ->
                        val json = gson.fromJson(topicMessage.payload, Trip::class.java)
                        completeTrip.postValue(json)
                        Timber.tag("huan completeTrip").d(json.toString())
                    }, {
                        Timber.tag("WebSocket completeTrip ").e(it.printStackTrace().toString())
                    }
                ),

            mStompClient?.topic("/topic/locationDriver/" + BasePrefers.getPrefsInstance().infoUser?.id)
                ?.subscribe(
                    { topicMessage: StompMessage ->
                        val json = gson.fromJson(topicMessage.payload, LocationSend::class.java)
                        locationDriver.postValue(json)
                        Timber.tag("huan locationDriver").d(topicMessage.payload)
                    }, {
                        Timber.tag("WebSocket").e(it.printStackTrace().toString())
                    }
                ),
            mStompClient?.topic("/topic/driver/request/" + BasePrefers.getPrefsInstance().infoDriver?.id)
                ?.subscribe(
                    { topicMessage: StompMessage ->
                        if (topicMessage.payload.toInt() == 0) {
                            isDriverAcceptTripSuccess.postValue(false)
                        } else {
                            isDriverAcceptTripSuccess.postValue(true)
                        }
                    }, {
                        Timber.tag("WebSocket").e(it.printStackTrace().toString())
                    }
                )
        )
    }

    @SuppressLint("CheckResult")
    fun sendRequest(trip: Trip) {
        val gson = Gson()
        val json = gson.toJson(trip)
        mStompClient?.send("/app/trip", json)?.subscribe(
            {

            }, {
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )

    }

    @SuppressLint("CheckResult")
    fun acceptTrip(trip: Trip) {
        val gson = Gson()
        val json = gson.toJson(trip)
        mStompClient?.send("/app/driver", json)?.subscribe(
            {

            }, {
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )
    }

    @SuppressLint("CheckResult")
    fun cancelTrip(trip: Trip) {
        val gson = Gson()
        val json = gson.toJson(trip)
        mStompClient?.send("/app/cancel", json)?.subscribe(
            {

            }, {
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )
    }

    @SuppressLint("CheckResult")
    fun completeTrip(trip: Trip) {
        val gson = Gson()
        val json = gson.toJson(trip)
        mStompClient?.send("/app/completeTrip", json)?.subscribe(
            {

            }, {
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )
    }

    @SuppressLint("CheckResult")
    fun sendLocation(location: LocationSend) {
        val gson = Gson()
        val json = gson.toJson(location)
        mStompClient?.send("/app/locationDriver", json)?.subscribe(
            {

            }, {
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )
    }

    fun disposableLocation() {
        disposable.dispose()
    }

    fun checkConnected(): Boolean {
        return mStompClient?.isConnected ?: false
    }

    @SuppressLint("CheckResult")
    fun disconnect() {
        // disposable.dispose()
        mStompClient?.disconnect()
    }

}