package com.example.projecttravel.data.uistates

import com.example.projecttravel.model.CheckSingleDayTrip
import com.example.projecttravel.model.TourAttractionAll
import com.example.projecttravel.model.SpotDtoResponse
import com.example.projecttravel.model.WeatherResponseGet
import java.time.LocalDate

data class PlanUiState(
    val weatherSwitch: Boolean = false,
    val planDateRange: ClosedRange<LocalDate>? = null,
    val currentPlanDate: LocalDate? = null,
    val planTourAttractionAll: List<TourAttractionAll> = emptyList(),
    val dateToWeather: List<WeatherResponseGet> = emptyList(),
    val dateToAttrByWeather: List<SpotDtoResponse> = emptyList(),
    val dateToAttrByCity: List<SpotDtoResponse> = emptyList(),
    val dateToSelectedTourAttrMap: Map<LocalDate, List<TourAttractionAll>> = emptyMap(),
    val checkSingleDayGps: CheckSingleDayTrip? = null,
)
