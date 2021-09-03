package com.example.pegaproject.model

data class Stock(
    var symbol: String,
    val name: String,
    val price: Int,
    val low: Int,
    val high: Int,
)
