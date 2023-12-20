package com.example.projecttravel.ui.screens.searchplace.searchapi

import android.content.Context
import android.util.Log
import com.example.projecttravel.BuildConfig
import com.example.projecttravel.data.RetrofitBuilderString
import com.example.projecttravel.data.RetrofitBuilderStringCustom
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.model.SearchedPlace
import com.example.projecttravel.model.TourAttractionSearchInfo
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesStatusCodes
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

/** Function for finding Searched Place from DB by name ====================*/
fun findSearchListByName(
    searchName: String?,
    tourAttrSearchList: List<TourAttractionSearchInfo>,
): TourAttractionSearchInfo? {
    val searched = tourAttrSearchList.find { it.name == searchName }
    return searched
}

/** Function for finding placeInfos by placeId ====================*/
suspend fun getPlaceInfo(
    searchedPlaceId: String,
    context: Context,
): SearchedPlace? {
    return suspendCancellableCoroutine { continuation ->
        // GOOGLE_MAPS_API_KEY 키 초기화
        val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY
        Places.initialize(context, apiKey)
        // Places API 초기화
        val placesClient = Places.createClient(context)

        // 받아올 필드 값들을 특정함.
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
        )

        // FetchPlaceRequest 객체 생성. placeId 와 필드 값을 넘겨준다
        val request = FetchPlaceRequest.newInstance(searchedPlaceId, placeFields)

        // request 객체를
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place
                val name = place.name
                val latLng = place.latLng
                val formattedAddress = place.address
                val searchedPlace = SearchedPlace(name = name, address = formattedAddress, latLng = latLng)
                continuation.resume(searchedPlace)
            }
            .addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("jiman=111GPS", "Place not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    when (statusCode) {
                        // 상태 코드에 따른 처리
                        PlacesStatusCodes.API_NOT_CONNECTED -> {
                            Log.e("jiman=222GPS", "API_NOT_CONNECTED")
                            continuation.resume(null)
                        }
                        else -> {
                            Log.e("jiman=333GPS", "Unknown error")
                            continuation.resume(null)
                        }
                    }
                }
            }
    }
}

/** Function for sending placeName to Django to request placeImg ====================*/
suspend fun sendPlaceNameDjango(
    selectUiState: SelectUiState,
    searchedPlace: SearchedPlace,
    stateInOut: String,
): String? {
    return suspendCancellableCoroutine { continuation ->
        Log.d("jiman=111sendNameDjango", selectUiState.selectCity?.cityId.toString())
        Log.d("jiman=111sendNameDjango", searchedPlace.name.toString())
        // timeout error solved
        val call = RetrofitBuilderStringCustom.travelStringApiCustomService
            .setPlaceName(placeName = searchedPlace.name, cityId = selectUiState.selectCity?.cityId, stateInOut = stateInOut)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("jiman=111Django", "Request Success + Response Success")
                        Log.d("jiman=111Django", call.toString())
                        Log.d("jiman=111Django", response.body().toString())
                        continuation.resume(loginResponse) // 작업 성공 시 true 반환
//                        continuation.resume(false) // 오류 확인용 false
                    } else {
                        Log.d("jiman=222Django", "Response body is null")
                        continuation.resume(null)
                    }
                } else {
                    Log.d("jiman=333Django", "Failure")
                    continuation.resume(null)
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("jiman=444Django", t.localizedMessage ?: "Unknown error")
                continuation.resume(null)
            }
        })
    }
}


/** ===Dump?============================================================================= */
/** Function for sending InOut number by string to write DB ==================== */
fun sendInOut(
    placeName: String?,
    stateInOut: String?
) {
    if (placeName != null && stateInOut != null) {
        val call = RetrofitBuilderString.travelStringApiService.setInOut(placeName = placeName, stateInOut = stateInOut)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // 통신 및 응답 성공
                    Log.d("jiman=111", "Request Success + Response Success")
                    Log.d("jiman=111", call.toString())
                    Log.d("jiman=111", response.body().toString())
                } else {
                    // 통신은 성공하였으나 응답 실패
                    Log.d("jiman=222", "Request Success + Response FAILURE")
                    Log.d("jiman=222", response.body().toString())
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                // 통신 실패
                Log.d("jiman=333", "Request FAILURE")
                Log.d("jiman=333", "checking: $call")
            }
        })
    } else {
        // placeName 또는 stateInOut이 null일 때 처리
        Log.d("jiman=444", "placeName or stateInOut is null")
    }
}
