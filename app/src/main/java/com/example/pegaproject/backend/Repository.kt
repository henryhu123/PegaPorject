package com.example.pegaproject.backend

import com.example.pegaproject.utils.StockApiDeserializer
import com.example.pegaproject.model.StockApiResponse
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Repository {

    private val stockApi: StockApi by lazy {
        retrofit.create(StockApi::class.java)
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://71iztxw7wh.execute-api.us-east-1.amazonaws.com/interview/")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(buildGsonConverter())
            .build()
    }

    private val okHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun buildGsonConverter(): GsonConverterFactory {
        val myGson =
            GsonBuilder().registerTypeAdapter(StockApiResponse::class.java, StockApiDeserializer())
                .create()
        return GsonConverterFactory.create(myGson)
    }

    fun fetchFavStock(): Observable<StockApiResponse> {
        return stockApi.getFavStock()
    }

}