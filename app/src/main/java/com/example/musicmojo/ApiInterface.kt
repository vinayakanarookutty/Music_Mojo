package com.example.musicmojo
import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Headers
interface ApiInterface {
    @Headers("X-RapidAPI-Key: 3dc707c69emsh9e12bded8711213p1dd3a4jsn335389edff6d",
        "X-RapidAPI-Host:deezerdevs-deezer.p.rapidapi.com")
    @GET("search")
    fun getData(@Query("q")query: String): Call<MyData>
}