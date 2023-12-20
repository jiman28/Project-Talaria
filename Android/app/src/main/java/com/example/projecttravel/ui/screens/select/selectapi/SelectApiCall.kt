package com.example.projecttravel.ui.screens.select.selectapi

import android.util.Log
import com.example.projecttravel.data.RetrofitBuilderJson
import com.example.projecttravel.data.uistates.SelectUiState
import com.example.projecttravel.model.GetAttrWeather
import com.example.projecttravel.model.PlansDataRead
import com.example.projecttravel.model.SpotDtoRead
import com.example.projecttravel.model.SpotDtoResponse
import com.example.projecttravel.model.WeatherCallSend
import com.example.projecttravel.model.WeatherResponseGet
import com.example.projecttravel.model.TourAttractionInfo
import com.example.projecttravel.model.TourAttractionSearchInfo
import com.example.projecttravel.ui.viewmodels.PlanPageViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

/** ======================================================================================== */
/** asynchronous codes ===================================================================== */
/** function for getting Weather ====================*/
suspend fun getDateToWeather(
//    selectUiState: SelectUiState,
    selection: Any,
    planPageViewModel: PlanPageViewModel
): Boolean {

    val firstCity: Any? = when (selection) {
        is SelectUiState -> selection.selectTourAttractions.first()
        is PlansDataRead -> selection.plans[0].list[0]
        else -> null
    }

    val weatherCallSend = when (selection) {
        is SelectUiState -> selection.selectDateRange?.start.toString()
        is PlansDataRead -> selection.startDay

        else -> null
    }?.let {
        when (selection) {
            is SelectUiState -> selection.selectDateRange?.endInclusive.toString()
        is PlansDataRead -> selection.endDay

            else -> null
        }?.let { it1 ->
            WeatherCallSend(
            startDate = it,
            endDate = it1,
            lat = when (firstCity) {
                is TourAttractionSearchInfo -> firstCity.lat
                is TourAttractionInfo -> firstCity.lat
                is SpotDtoRead -> firstCity.lat
                else -> "몰루"
            },
            lng = when (firstCity) {
                is TourAttractionSearchInfo -> firstCity.lng
                is TourAttractionInfo -> firstCity.lan
                is SpotDtoRead -> firstCity.lan
                else -> "몰루"
            }
        )
        }
    }

//    val firstCity = selectUiState.selectTourAttractions.first()
//    val weatherCallSend = WeatherCallSend(
//        startDate = selectUiState.selectDateRange?.start.toString(),
//        endDate = selectUiState.selectDateRange?.endInclusive.toString(),
//        lat = when (firstCity) {
//            is TourAttractionSearchInfo -> firstCity.lat
//            is TourAttractionInfo -> firstCity.lat
//            else -> "몰루"
//        },
//        lng = when (firstCity) {
//            is TourAttractionSearchInfo -> firstCity.lng
//            is TourAttractionInfo -> firstCity.lan
//            else -> "몰루"
//        }
//    )

    return suspendCancellableCoroutine { continuation ->
        weatherCallSend?.let { RetrofitBuilderJson.travelJsonApiService.getDateWeather(it) }
            ?.enqueue(object : Callback<List<WeatherResponseGet>> {
                override fun onResponse(
                    call: Call<List<WeatherResponseGet>>,
                    response: Response<List<WeatherResponseGet>>
                ) {
                    if (response.isSuccessful) {
                        val weatherResponse = response.body()
                        if (weatherResponse != null) {
                            planPageViewModel.setDateToWeather(weatherResponse)
                            Log.d("jiman=111", "Request Success + Response Success")
                            Log.d("jiman=111", call.toString())
                            Log.d("jiman=111", response.body().toString())
                            continuation.resume(true) // 작업 성공 시 true 반환
                            //                        continuation.resume(false) // 오류 확인용 false
                        } else {
                            Log.d("jiman=222", "Response body is null")
                            continuation.resume(false) // 작업 실패 시 false 반환
                        }
                    } else {
                        Log.d("jiman=333", "Failure")
                        continuation.resume(false) // 작업 실패 시 false 반환
                    }
                }

                override fun onFailure(call: Call<List<WeatherResponseGet>>, t: Throwable) {
                    Log.d("jiman=444", t.localizedMessage ?: "Unknown error")
                    continuation.resume(false) // 작업 실패 시 false 반환
                }
            })
    }
}

/** function for getting Weather ====================*/
suspend fun getDateToAttrByWeather(
    selectUiState: SelectUiState,
    planPageViewModel: PlanPageViewModel,
): Boolean {
    val selections = selectUiState.selectTourAttractions
    val placeId = selections
        .filterIsInstance<TourAttractionInfo>() // TourAttractionInfo 타입만 선택
        .joinToString(",") { it.placeId } // placeId 값으로 join
    val finds = selections
        .filterIsInstance<TourAttractionSearchInfo>() // TourAttractionSearchInfo 타입만 선택
        .joinToString(",") { it.name } // name 값으로 join

    val selectedDate = "${selectUiState.selectDateRange?.start.toString()} to ${selectUiState.selectDateRange?.endInclusive.toString()}"

    val getAttrWeather = GetAttrWeather(
        placeId = placeId,
        finds = finds,
        selectedDate = selectedDate,
    )
    val call = RetrofitBuilderJson.travelJsonApiService.getDateWeatherAttr(
        getAttrWeather
    )
    return suspendCancellableCoroutine { continuation ->
        call.enqueue(object : Callback<List<SpotDtoResponse>> {
            override fun onResponse(
                call: Call<List<SpotDtoResponse>>,
                response: Response<List<SpotDtoResponse>>,
            ) {
                if (response.isSuccessful) {
                    // 통신 및 응답 성공
                    val attrResponse = response.body()
                    if (attrResponse != null) {
                        planPageViewModel.setDateToAttrByWeather(attrResponse)
                        Log.d("jiman=111getDateToAttrByWeather", "Request Success + Response Success")
                        Log.d("jiman=111getDateToAttrByWeather", response.body().toString())
                        continuation.resume(true) // 작업 성공 시 true 반환
                    } else {
                        Log.d("jiman=222getDateToAttrByWeather", "Response body is null")
                        continuation.resume(false) // 작업 실패 시 false 반환
                    }
                } else {
                    // 통신은 성공하였으나 응답 실패
                    Log.d("jiman=333getDateToAttrByWeather", "Request Success + Response FAILURE")
                    Log.d("jiman=333getDateToAttrByWeather", response.body().toString())
                    continuation.resume(false) // 작업 실패 시 false 반환
                }
            }
            override fun onFailure(call: Call<List<SpotDtoResponse>>, t: Throwable) {
                // 통신 실패
                Log.d("jiman=444getDateToAttrByWeather", "Request FAILURE")
                Log.d("jiman=444getDateToAttrByWeather", "checking: $call")
                continuation.resume(false) // 작업 실패 시 false 반환
            }
        })
    }
}

/** function for getting Weather ====================*/
suspend fun getDateToAttrByCity(
    selectUiState: SelectUiState,
    planPageViewModel: PlanPageViewModel,
): Boolean {
    val selections = selectUiState.selectTourAttractions
    val placeId = selections
        .filterIsInstance<TourAttractionInfo>() // TourAttractionInfo 타입만 선택
        .joinToString(",") { it.placeId } // placeId 값으로 join
    val finds = selections
        .filterIsInstance<TourAttractionSearchInfo>() // TourAttractionSearchInfo 타입만 선택
        .joinToString(",") { it.name } // name 값으로 join

    val selectedDate = "${selectUiState.selectDateRange?.start.toString()} to ${selectUiState.selectDateRange?.endInclusive.toString()}"

    val getAttrWeather = GetAttrWeather(
        placeId = placeId,
        finds = finds,
        selectedDate = selectedDate,
    )
    val call = RetrofitBuilderJson.travelJsonApiService.getDateCityAttr(
        getAttrWeather
    )
    return suspendCancellableCoroutine { continuation ->
        call.enqueue(object : Callback<List<SpotDtoResponse>> {
            override fun onResponse(
                call: Call<List<SpotDtoResponse>>,
                response: Response<List<SpotDtoResponse>>,
            ) {
                if (response.isSuccessful) {
                    // 통신 및 응답 성공
                    val attrResponse = response.body()
                    if (attrResponse != null) {
                        planPageViewModel.setDateToAttrByCity(attrResponse)
                        Log.d("jiman=111getDateToAttrByCity", "Request Success + Response Success")
                        Log.d("jiman=111getDateToAttrByCity", response.body().toString())
                        continuation.resume(true) // 작업 성공 시 true 반환
                    } else {
                        Log.d("jiman=222getDateToAttrByCity", "Response body is null")
                        continuation.resume(false) // 작업 실패 시 false 반환
                    }
                } else {
                    // 통신은 성공하였으나 응답 실패
                    Log.d("jiman=333getDateToAttrByCity", "Request Success + Response FAILURE")
                    Log.d("jiman=333getDateToAttrByCity", response.body().toString())
                    continuation.resume(false) // 작업 실패 시 false 반환
                }
            }
            override fun onFailure(call: Call<List<SpotDtoResponse>>, t: Throwable) {
                // 통신 실패
                Log.d("jiman=444getDateToAttrByCity", "Request FAILURE")
                Log.d("jiman=444getDateToAttrByCity", "checking: $call")
                continuation.resume(false) // 작업 실패 시 false 반환
            }
        })
    }
}
