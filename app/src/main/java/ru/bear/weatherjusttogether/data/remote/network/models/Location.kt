package ru.bear.weatherjusttogether.data.remote.network.models

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val url: String,
    val tz_id: String,
    val localtime_epoch: Long,
    val localtime: String
)