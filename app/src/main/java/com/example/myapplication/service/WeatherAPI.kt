package com.example.myapplication.service

import com.example.myapplication.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather?&units=metric&APPID=ff6b0f2f1d1fd2f65d9a0bcf92785f7e&fbclid=IwAR0NzgxoQ6uG46Z1UaOGYfPugU0mtpEEKimMIMCLaEfuVAr3N6nzHAJdrwI")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>

}