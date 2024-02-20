package com.bkplus.callscreen.ui.splash.welcome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrison.myapplication.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {
    private val _categoriesLiveData = MutableLiveData<List<Category>>(listOf())
    val categoriesLiveData = _categoriesLiveData
    fun fetchCategories() {
        viewModelScope.launch {
            val categories = listOf(
                Category("Animal", R.drawable.animal),
                Category("Anime", R.drawable.anime),
                Category("Galaxy", R.drawable.galaxy),
                Category("Illustrator Art", R.drawable.illustrator_art),
                Category("Cartoon", R.drawable.cartoon),
                Category("Food", R.drawable.food),
                Category("Festival", R.drawable.festival),
                Category("Movie", R.drawable.movie),
                Category("Car", R.drawable.car),
                Category("Halloween", R.drawable.halloween),
                Category("Love", R.drawable.love),
                Category("Christmas", R.drawable.christmas),
                Category("Neon", R.drawable.neon),
                Category("Hero", R.drawable.hero),
            )
            _categoriesLiveData.postValue(categories)
        }
    }
}
