package com.example.pegaproject.utils

import com.example.pegaproject.model.Status
import com.example.pegaproject.model.StockApiResponse
import com.example.pegaproject.model.Stock
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class StockApiDeserializer : JsonDeserializer<StockApiResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): StockApiResponse {
        val gson = Gson()
        val mapType = object : TypeToken<Map<String, Stock>>() {}.type
        val stockMap: Map<String, Stock> = gson.fromJson(json, mapType)
        return StockApiResponse(Status.LOADING, stockMap)
    }
}