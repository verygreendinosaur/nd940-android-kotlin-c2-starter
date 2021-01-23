package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.api.AsteroidRadarApi
import com.udacity.asteroidradar.api.getCurrentDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.AsteroidContainer
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabaseDao) {

    enum class AsteroidFilter {
        TODAY, WEEK, FROM_TODAY, ALL_SAVED
    }

    var filter = MutableLiveData<AsteroidFilter>(AsteroidFilter.FROM_TODAY)

    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(filter) { view ->
        when(view) {
            AsteroidFilter.TODAY -> todayAsteroids
            AsteroidFilter.WEEK -> weekAsteroids
            AsteroidFilter.FROM_TODAY -> asteroidsFromToday
            AsteroidFilter.ALL_SAVED -> savedAsteroids
        }
    }

    private val savedAsteroids: LiveData<List<Asteroid>> = Transformations
            .map(database.getAllAsteroids()) {
                it.asDomainModel()
            }

    private val asteroidsFromToday: LiveData<List<Asteroid>> = Transformations
            .map(database.getAllAsteroidsFromToday()) {
                it.asDomainModel()
            }

    private val todayAsteroids: LiveData<List<Asteroid>> = Transformations
            .map(database.getAllAsteroidsToday()) {
                it.asDomainModel()
            }

    private val weekAsteroids: LiveData<List<Asteroid>> = Transformations
            .map(database.getAsteroidsThisWeek()) {
                it.asDomainModel()
            }

    fun updateFilter(newFilter: AsteroidFilter) {
        filter.value = newFilter
    }

    suspend fun prune() {
        withContext(Dispatchers.IO) {
            database.clearOld()
        }
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            var result = AsteroidRadarApi.retrofitService.getAsteroids()
            val json = JSONObject(result)
            val asteroids = AsteroidContainer(asteroids = parseAsteroidsJsonResult(json))

            database.insert(*asteroids.asDatabaseModel())
        }
    }

}