package com.retro_fit.animal.model

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("animals/rand")
    fun getRandomAnimal(): Call<Animal>
}