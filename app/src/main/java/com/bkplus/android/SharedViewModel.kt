package com.bkplus.android

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.android.api.ApiService
import com.bkplus.android.api.onException
import com.bkplus.android.api.onSuccess
import com.bkplus.android.model.Driver
import com.bkplus.android.model.Trip
import com.bkplus.android.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    val historyUserLiveData = MutableLiveData<ArrayList<Trip>>()
    val historyDriverLiveData = MutableLiveData<ArrayList<Trip>>()

    fun getListHistory(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getHistoryUser(user).onSuccess {
                it.data?.let { data ->
                    historyUserLiveData.postValue(data)
                }

            }.onException {
                Log.d("huanhuan", it?.message.toString())
            }
        }

    }


    fun getListHistoryDriver(driver: Driver) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getHistoryDriver(driver).onSuccess {
                it.data?.let { data ->
                    historyDriverLiveData.postValue(data)
                }

            }.onException {
                Log.d("huanhuan", it?.message.toString())
            }
        }

    }



}