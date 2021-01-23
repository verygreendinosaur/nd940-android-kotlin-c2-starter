package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.api.getCurrentDate
import com.udacity.asteroidradar.api.getSevenDaysFromCurrentDate

@Dao
interface AsteroidDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg asteroids: DatabaseAsteroid)

    @Update
    suspend fun update(asteroid: DatabaseAsteroid)

    @Query("SELECT * from asteroid_table WHERE id = :id")
    suspend fun get(id: Long): DatabaseAsteroid?

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()

    @Query("DELETE FROM asteroid_table WHERE close_approach_date < :today")
    suspend fun clearOld(today: String = getCurrentDate())

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date >= :today AND close_approach_date <= :oneWeekFromNow ORDER BY close_approach_date ASC")
    fun getAsteroidsThisWeek(today: String = getCurrentDate(), oneWeekFromNow: String = getSevenDaysFromCurrentDate()): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date >= :today ORDER BY close_approach_date ASC")
    fun getAllAsteroidsFromToday(today: String = getCurrentDate()): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date = :today ORDER BY close_approach_date ASC")
    fun getAllAsteroidsToday(today: String = getCurrentDate()): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table ORDER BY close_approach_date ASC")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>
}
