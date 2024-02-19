package com.bkplus.callscreen.ui.main.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.callscreen.api.ApiService
import com.bkplus.callscreen.api.entity.HomeSectionEntity
import com.bkplus.callscreen.api.onSuccess
import com.harison.core.app.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {

     val homeSectionLiveData = SingleLiveData<ArrayList<HomeSectionEntity>>()
    fun getHomeSection(){
        viewModelScope.launch {
            apiService.getApiData().onSuccess {
                homeSectionLiveData.postValue(it)
            }
        }
    }
}