package com.example.pegaproject.backend

import com.example.pegaproject.model.StockApiResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface StockApi {

    @GET("favorite-stocks")
    fun getFavStock(): Observable<StockApiResponse>
}