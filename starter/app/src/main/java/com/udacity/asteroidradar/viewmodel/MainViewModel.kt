package com.udacity.asteroidradar.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.api.NasaImageApi
import com.udacity.asteroidradar.api.getCurrentDate
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsRepository

import kotlinx.coroutines.launch

import java.lang.Exception

class MainViewModel(val database: AsteroidDatabaseDao, application: Application) : ViewModel() {

    private val pictureOfDayTitleDefaultText = application.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    private val pictureOfDayTitleText = application.getString(R.string.nasa_picture_of_day_content_description_format)
    private val generalNetworkErrorText = application.getString(R.string.general_network_error)

    private val asteroidsRepository = AsteroidsRepository(database)

    // Internal MutableLiveData

    private val _pictureOfDayUrl = MutableLiveData<String>()

    private val _pictureOfDayTitle = MutableLiveData<String>(pictureOfDayTitleDefaultText)

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()

    private val _toastMessage = MutableLiveData<String>()

    // External Immutable LiveData

    val pictureOfDayUrl: LiveData<String>
        get() = _pictureOfDayUrl

    val pictureOfDayTitle: LiveData<String>
        get() = _pictureOfDayTitle

    val navigateToAsteroid
        get() = _navigateToAsteroid

    val toastMessage
        get() = _toastMessage

    var asteroids: LiveData<List<Asteroid>> = asteroidsRepository.asteroids

    init {
        fetchAsteroids()
        getImage()
    }

    private fun fetchAsteroids() {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
                _toastMessage.value = generalNetworkErrorText
            }
        }
    }

    fun onAsteroidTapped(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToAsteroid.value = null
    }

    fun onToastDisplayed() {
        _toastMessage.value = null
    }

    fun showAllSaved() {
        asteroidsRepository.updateFilter(AsteroidsRepository.AsteroidFilter.ALL_SAVED)
    }

    fun showWeek() {
        asteroidsRepository.updateFilter(AsteroidsRepository.AsteroidFilter.WEEK)
    }

    fun showToday() {
        asteroidsRepository.updateFilter(AsteroidsRepository.AsteroidFilter.TODAY)
    }

    private fun getImage() {
        viewModelScope.launch {
            try {
                var result = NasaImageApi.retrofitService.getImageOfTheDay()
                if (result.mediaType == "image") {
                    _pictureOfDayUrl.value = result.url
                    _pictureOfDayTitle.value = pictureOfDayTitleText.format(result.title)
                }
            } catch (e: Exception) {
                // Fail silently at this time.
            }
        }
    }

}
