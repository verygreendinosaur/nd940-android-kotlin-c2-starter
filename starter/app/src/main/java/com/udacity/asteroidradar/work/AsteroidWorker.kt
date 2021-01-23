package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.getCurrentDate
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import java.lang.Exception


class AsteroidWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext).asteroidDatabaseDao
        val repository = AsteroidsRepository(database)

        return try {
            repository.prune()
            repository.refreshAsteroids()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "AsteroidWorker"
    }

}
