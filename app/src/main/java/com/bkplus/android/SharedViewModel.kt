package com.bkplus.android

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.android.api.ApiService
import com.bkplus.android.api.onException
import com.bkplus.android.api.onSuccess
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
    val historyLiveData = MutableLiveData<ArrayList<Trip>>()

    fun getListHistory(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getHistoryUser(user).onSuccess {
                it.data?.let { data ->
                    historyLiveData.postValue(data)
                }

            }.onException {
                Log.d("huanhuan", it?.message.toString())
            }
        }

    }

}