package com.example.projecttravel.model

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class CityInfo (
    val cityId: String,
    val cityName: String,
    val countryId: String,
)

@Serializable
data class CountryInfo (
    val countryId: String,
    val countryInfo: String,
    val countryName: String,
    val currency: String,
    val imageC: String,
    val languageC: String,
    val timeD: String,
)

interface TourAttractionAll {
    /** interface with All TourAttractionInfo + TourAttractionSearchInfo
     * this makes selectTourAttractions include other types of lists*/
}

@Serializable
data class TourAttractionInfo (
    val placeId: String,
    val imageP: String,
    val inOut: String,
    val lan: String,
    val lat: String,
    val placeAddress: String,
    val placeName: String,
    val placeType: String,
    val price: String,
    val cityId: String,
    val interestId: String,
): TourAttractionAll

@Serializable
data class TourAttractionSearchInfo (
    val name: String,
    val Address: String,    // 카멜 표기법좀 제발
    val globalCode: String,
    val compoundCode: String,
    val lat: String,
    val lng: String,
    val img: String,
    val inOut: String,
    val cityId: String,
): TourAttractionAll

@Serializable
data class InterestInfo (
    val interestId: String,
    val interestType: String,
    val interestImg: String,
    // 다음에 이 데이터가 안 뽑히면 jar 파일에서 카멜표기법으로 제대로 변환시켜서 그런거임
)

data class SearchedPlace(
    val name: String? = null,
    val address: String? = null,
    val latLng: LatLng? = null, // 이 값은 Serializable 어노테이션이 불가능함
)
