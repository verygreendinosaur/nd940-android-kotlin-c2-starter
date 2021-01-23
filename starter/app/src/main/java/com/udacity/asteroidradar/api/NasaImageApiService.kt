package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// BASE URLS
private const val BASE_URL = "https://api.nasa.gov/"

// ENDPOINTS
private const val APOD_ENDPOINT = "planetary/apod"

// QUERY PARAMS
private const val API_KEY = BuildConfig.NASA_API_KEY

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface NasaImageApiService {
    @GET(APOD_ENDPOINT)
    suspend fun getImageOfTheDay(
            @Query("api_key") apiKey: String = API_KEY
    ): PictureOfDay
}

object NasaImageApi {
    val retrofitService : NasaImageApiService by lazy {
        retrofit.create(NasaImageApiService::class.java)
    }
}
