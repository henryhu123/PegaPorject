package com.example.pegaproject.model

data class StockApiResponse(
    var status: Status,
    val map: Map<String, Stock>
)

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
}