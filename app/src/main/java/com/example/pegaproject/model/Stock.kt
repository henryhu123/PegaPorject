package com.example.pegaproject.model

data class Stock(
    var symbol: String= "",
    val name: String = "",
    val price: Double = 0.0,
    val low: Double = 0.0,
    val high: Double = 0.0,
    var color:String = "grey",
)
