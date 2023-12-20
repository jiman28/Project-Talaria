package com.example.projecttravel.ui.viewmodels

import android.location.Geocoder
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** data class for saving Auto Complete Data Results ====================*/
data class AutocompleteResult (
    val placeId: String,
    val name: String,
)

/** ViewModel for AutocompleteResult ====================*/
class LocationViewModel : ViewModel() {
    // placesClient,geoCoder = Google Places API와 상호 작용하기 위한 클라이언트 객체
    lateinit var placesClient: PlacesClient
    lateinit var geoCoder: Geocoder
    // 위치 검색 결과를 저장
    val locationAutofill = mutableStateListOf<AutocompleteResult>()
    // Coroutine 작업을 추적하기 위한 Job 객체
    private var job: Job? = null

    // searchPlaces = 검색 쿼리(query)에 대한 위치 검색을 수행, 결과를 locationAutofill 리스트에 추가
    fun searchPlaces(query: String) {
        job?.cancel()
        locationAutofill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    locationAutofill += response.autocompletePredictions.map {
                        AutocompleteResult(
                            name = it.getFullText(null).toString(),
                            placeId = it.placeId,
                        )
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }
        }
    }
}
