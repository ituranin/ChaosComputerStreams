package com.igtuapps.chaoscomputerstreams.network

import retrofit2.http.GET

interface ApiService {
    @GET("streams/v2.json")
    suspend fun getConferences(): List<Conference>
}