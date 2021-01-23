package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// BASE URLS
private const val BASE_URL = "https://api.nasa.gov/"

// ENDPOINTS
private const val NEO_ASTEROID_ENDPOINT = "neo/rest/v1/feed"

// QUERY PARAMS
private const val API_KEY = BuildConfig.NASA_API_KEY

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface AsteroidRadarApiService {
    @GET(NEO_ASTEROID_ENDPOINT)
    suspend fun getAsteroids(
            @Query("start_date") startDate: String = getCurrentDate(),
            @Query("api_key") apiKey: String = API_KEY
    ): String
}

object AsteroidRadarApi {
    val retrofitService : AsteroidRadarApiService by lazy {
        retrofit.create(AsteroidRadarApiService::class.java)
    }
}

