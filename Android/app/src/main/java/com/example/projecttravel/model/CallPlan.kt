package com.example.projecttravel.model

import kotlinx.serialization.Serializable

// Container for saving to Server
@Serializable
data class PlansData (
    var planName: String,
    var email: String,
    var startDay: String,
    var endDay: String,
    var plans: List<SpotDtoResponse>
)

@Serializable
data class SpotDtoResponse(
    var date: String,
    var list: List<SpotDto>
): CheckSingleDayTrip

@Serializable
data class SpotDto (
    var pk: String,
    var name: String,
    var img: String,
    var lan: String,
    var lat: String,
    var inOut: String,
    var cityId: String,
)

@Serializable
data class WeatherCallSend (
    var startDate: String,
    var endDate: String,
    var lat: String,
    var lng: String,
)

@Serializable
data class WeatherResponseGet (
    val day: String?,
    val inOut: String?,
    val icon: String?,
)

@Serializable
data class GetAttrWeather (
    var placeId: String,
    var finds: String,
    var selectedDate: String,
)

// Container for reading from Server in MyPage
@Serializable
data class PlansDataRead (
    var id: String,
    val planName: String,
    val email: String,
    val startDay: String,
    val endDay: String,
    val plans: List<SpotDtoResponseRead>
)

@Serializable
data class SpotDtoResponseRead(
    val date: String,
    val list: List<SpotDtoRead>
): CheckSingleDayTrip

@Serializable
data class SpotDtoRead (
    val pk: String,
    val name: String,
    val img: String,
    val lan: String,
    val lat: String,
    val inOut: Int,
    val cityId: Int,
)

// Container for checking Single Day
interface CheckSingleDayTrip {
}