package com.bkplus.android.websocket

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.bkplus.android.model.Trip
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocket @Inject constructor(){

    private var mStompClient: StompClient? = null

    val mutableLiveData = MutableLiveData<Trip>()
    val acceptTrip =  MutableLiveData<Trip>()
    private val disposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun connectWebSocket() {
        val gson = Gson()
        mStompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "wss://humble-topical-krill.ngrok-free.app/websocket"
        )
        mStompClient?.connect()

        disposable.addAll(
            mStompClient?.topic("/topic/trip/"+2)?.subscribe(
                { topicMessage: StompMessage ->
                    val json = gson.fromJson(topicMessage.payload,Trip :: class.java)
                    mutableLiveData.postValue(json)
                    Timber.tag("mutableLiveData").d(topicMessage.payload) }
                ,{
                    Timber.tag("WebSocket").e(it.printStackTrace().toString())
                }
            ),

            mStompClient?.topic("/topic/cancel/"+2)?.subscribe(
                { topicMessage: StompMessage ->
                    val json = gson.fromJson(topicMessage.payload,Trip :: class.java)
                    mutableLiveData.postValue(json)
                    Timber.tag("mutableLiveData").d(topicMessage.payload) }
                ,{
                    Timber.tag("WebSocket").e(it.printStackTrace().toString())
                }
            )
        )


        mStompClient?.topic("/topic/driver/352")?.subscribe(
            { topicMessage: StompMessage ->
                val json = gson.fromJson(topicMessage.payload,Trip :: class.java)
                acceptTrip.postValue(json)
                Timber.tag("huan driver accepted").d(topicMessage.payload) }
            ,{
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )


    }

     @SuppressLint("CheckResult")
     fun sendRequest(trip: Trip){
        val gson = Gson()
        val json = gson.toJson(trip)
        mStompClient?.send("/app/trip", json)?.subscribe(
            {

            },{
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )

    }

    @SuppressLint("CheckResult")
    fun acceptTrip(trip: Trip){
        val gson = Gson()
        val json = gson.toJson(trip)
        mStompClient?.send("/app/driver", json)?.subscribe(
            {

            },{
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )
    }

    @SuppressLint("CheckResult")
    fun cancelTrip(trip: Trip){
        val gson = Gson()
        val json = gson.toJson(trip)
        mStompClient?.send("/app/cancel", json)?.subscribe(
            {

            },{
                Timber.tag("WebSocket").e(it.printStackTrace().toString())
            }
        )
    }

    @SuppressLint("CheckResult")
    fun disconnect() {
        disposable.dispose()
        mStompClient?.disconnect()
    }

}