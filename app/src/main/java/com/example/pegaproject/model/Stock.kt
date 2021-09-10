package com.example.pegaproject.model

data class Stock(
    var symbol: String,
    val name: String,
    val price: Double,
    val low: Double,
    val high: Double,
    var color:String = "Grey",
)
