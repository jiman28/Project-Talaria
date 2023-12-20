package com.example.projecttravel.data.uistates

import com.example.projecttravel.model.TourAttractionSearchInfo
import com.example.projecttravel.model.CityInfo
import com.example.projecttravel.model.CountryInfo
import com.example.projecttravel.model.InterestInfo
import com.example.projecttravel.model.TourAttractionAll
import java.time.LocalDate

data class SelectUiState(
    // 객체를 저장함
    val selectDateRange: ClosedRange<LocalDate>? = null,
    val selectCountry: CountryInfo? = null,
    val selectCity: CityInfo? = null,
    val selectInterest: InterestInfo? = null,
    val selectSearch: TourAttractionSearchInfo? = null,
    val selectTourAttractions: List<TourAttractionAll> = emptyList(), // 기본적으로 빈 리스트로 초기화
)
