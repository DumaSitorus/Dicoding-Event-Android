package com.duma.dicodingevent.data.retrofit

import com.duma.dicodingevent.data.response.EventDetailResponse
import com.duma.dicodingevent.data.response.EventResponse
import retrofit2.http.*

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int
    ): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): EventDetailResponse

    @GET("events")
    suspend fun getNearestEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): EventResponse

    @GET("events")
    suspend fun searchEvents(
        @Query("active") active: Int,
        @Query("q") query: String
    ): EventResponse
}

