package com.bkplus.android

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.android.api.ApiService
import com.bkplus.android.api.onException
import com.bkplus.android.api.onFailure
import com.bkplus.android.api.onSuccess
import com.bkplus.android.model.Driver
import com.bkplus.android.model.RequestOtp
import com.bkplus.android.model.Trip
import com.bkplus.android.model.User
import com.harison.core.app.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    val historyUserLiveData = MutableLiveData<ArrayList<Trip>>()
    val historyDriverLiveData = MutableLiveData<ArrayList<Trip>>()
    val loginUserSuccessLiveData = SingleLiveData<User>()
    val loginFailLiveData = SingleLiveData<String>()
    val loginDriverSuccessLiveData = SingleLiveData<Driver>()

    val registerUserSuccessLiveData = SingleLiveData<User>()
    val registerUserFailLiveData = SingleLiveData<String>()
    val sendOtpUserSuccessLiveData = SingleLiveData<String>()
    val sendOtpUserFailLiveData = SingleLiveData<String>()
    val infoUserLiveData = SingleLiveData<User>()

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

    fun login(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.loginUser(user).onSuccess {
                if (it.data == null) loginFailLiveData.postValue(it.error.toString())
                it.data?.let { users ->
                    loginUserSuccessLiveData.postValue(users)
                }
            }.onFailure { code, message ->
                loginFailLiveData.postValue(message)
            }
        }
    }

    fun login(driver: Driver) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.loginDriver(driver).onSuccess {
                if (it.data == null) loginFailLiveData.postValue(it.error.toString())
                it.data?.let { driver ->
                    loginDriverSuccessLiveData.postValue(driver)
                }
            }.onFailure { code, message ->
                loginFailLiveData.postValue(message)
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.registerUser(user).onSuccess {
                if (it.data == null) registerUserFailLiveData.postValue(it.error.toString())
                it.data?.let { users ->
                    registerUserSuccessLiveData.postValue(users)
                }
            }.onFailure { code, message ->
                registerUserFailLiveData.postValue(message)
            }
        }
    }

    fun sendOtp(requestOtp: RequestOtp) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.sendOtp(requestOtp).onSuccess {
                if (it.data == null) sendOtpUserFailLiveData.postValue(it.message.toString())
                it.data?.let { otp ->
                    sendOtpUserSuccessLiveData.postValue(otp)
                }
            }.onFailure { code, message ->
                sendOtpUserFailLiveData.postValue(message)
            }
        }
    }

    fun voteDriver(driver: Driver, start: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.voteDriver(driver, start).onSuccess {

            }.onFailure { code, message ->
                sendOtpUserFailLiveData.postValue(message)
            }
        }
    }

    fun saveInfoUser(user: User, actionSuccess: () -> Unit, actionFail: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.saveInfoUser(user).onSuccess {
                actionSuccess.invoke()
            }.onFailure { code, message ->
                actionFail.invoke()
            }
        }
    }

    fun updateAvatar(
        avatar: MultipartBody.Part,
        user: RequestBody,
        actionSuccess: () -> Unit,
        actionFail: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.updateAvatarUser(avatar,user).onSuccess {
                actionSuccess.invoke()
            }.onFailure { code, message ->
                actionFail.invoke()
            }
        }
    }

    fun getInfoUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            apiService.infoUser(user).onSuccess {
                it?.data?.let { user ->
                    infoUserLiveData.postValue(user)
                }
            }.onFailure { code, message ->

            }
        }
    }

}