package ru.bear.weatherjusttogether.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_saved_city")
data class LastSavedCityDto(
    @PrimaryKey val city: String
)

