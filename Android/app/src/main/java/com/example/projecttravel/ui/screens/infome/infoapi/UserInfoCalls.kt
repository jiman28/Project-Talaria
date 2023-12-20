package com.example.projecttravel.ui.screens.infome.infoapi

import android.util.Log
import com.example.projecttravel.data.RetrofitBuilderJson
import com.example.projecttravel.model.CheckOtherUserById
import com.example.projecttravel.model.PlansDataRead
import com.example.projecttravel.model.UserInterest
import com.example.projecttravel.model.UserResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

/** ======================================================================================== */
/** asynchronous codes ===================================================================== */
// send Article To Db
suspend fun getPeopleLikeMe(
    userResponse: UserResponse,
): List<UserResponse> {
    return suspendCancellableCoroutine { continuation ->
        val call = RetrofitBuilderJson.travelJsonApiService.getLikeMeUser(userResponse)
        call.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("jiman=111", "Request Success + Response Success")
                        Log.d("jiman=111", call.toString())
                        Log.d("jiman=111", response.body().toString())
                        continuation.resume(loginResponse) // 작업 성공 시 true 반환
//                        continuation.resume(false) // 오류 확인용 false
                    } else {
                        Log.d("jiman=222", "Response body is null")
                        continuation.resume(emptyList()) // 작업 실패 시 false 반환
                    }
                } else {
                    Log.d("jiman=333", "Failure")
                    continuation.resume(emptyList()) // 작업 실패 시 false 반환
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Log.d("jiman=444", t.localizedMessage ?: "Unknown error")
                continuation.resume(emptyList()) // 작업 실패 시 false 반환
            }
        })
    }
}

/** ======================================================================================== */
/** asynchronous codes ===================================================================== */
// send Article To Db
suspend fun getUserPageById(
    checkOtherUserById: CheckOtherUserById,
): UserResponse? {
    return suspendCancellableCoroutine { continuation ->
        val call = RetrofitBuilderJson.travelJsonApiService.getOtherUserInfo(checkOtherUserById)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("jiman=111", "Request Success + Response Success")
                        Log.d("jiman=111", call.toString())
                        Log.d("jiman=111", response.body().toString())
                        continuation.resume(loginResponse) // 작업 성공 시 true 반환
//                        continuation.resume(false) // 오류 확인용 false
                    } else {
                        Log.d("jiman=222", "Response body is null")
                        continuation.resume(null) // 작업 실패 시 false 반환
                    }
                } else {
                    Log.d("jiman=333", "Failure")
                    continuation.resume(null) // 작업 실패 시 false 반환
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("jiman=444", t.localizedMessage ?: "Unknown error")
                continuation.resume(null) // 작업 실패 시 false 반환
            }
        })
    }
}

/** ======================================================================================== */
/** asynchronous codes ===================================================================== */
// send Article To Db
suspend fun callMyPlanList(
    userResponse: UserResponse,
): List<PlansDataRead> {
    return suspendCancellableCoroutine { continuation ->
        val call = RetrofitBuilderJson.travelJsonApiService.getMyPlanList(userResponse)
        call.enqueue(object : Callback<List<PlansDataRead>> {
            override fun onResponse(
                call: Call<List<PlansDataRead>>,
                response: Response<List<PlansDataRead>>
            ) {
                if (response.isSuccessful) {
                    val planResponse = response.body()
                    if (planResponse != null) {
                        Log.d("jiman=111", "Request Success + Response Success")
                        Log.d("jiman=111", call.toString())
                        Log.d("jiman=111", response.body().toString())
                        continuation.resume(planResponse) // 작업 성공 시 true 반환
//                        continuation.resume(false) // 오류 확인용 false
                    } else {
                        Log.d("jiman=222", "Response body is null")
                        continuation.resume(emptyList()) // 작업 실패 시 false 반환
                    }
                } else {
                    Log.d("jiman=333", "Failure")
                    continuation.resume(emptyList()) // 작업 실패 시 false 반환
                }
            }

            override fun onFailure(call: Call<List<PlansDataRead>>, t: Throwable) {
                Log.d("jiman=444", t.localizedMessage ?: "Unknown error")
                continuation.resume(emptyList()) // 작업 실패 시 false 반환
            }
        })
    }
}

/** ======================================================================================== */
/** asynchronous codes ===================================================================== */
// send Article To Db
suspend fun callMyInterest(
    userResponse: UserResponse,
): UserInterest? {
    return suspendCancellableCoroutine { continuation ->
        val call = RetrofitBuilderJson.travelJsonApiService.getMyInterest(userResponse)
        call.enqueue(object : Callback<UserInterest> {
            override fun onResponse(
                call: Call<UserInterest>,
                response: Response<UserInterest>
            ) {
                if (response.isSuccessful) {
                    val planResponse = response.body()
                    if (planResponse != null) {
                        Log.d("jiman=111", "Request Success + Response Success")
                        Log.d("jiman=111", call.toString())
                        Log.d("jiman=111", response.body().toString())
                        continuation.resume(planResponse) // 작업 성공 시 true 반환
//                        continuation.resume(false) // 오류 확인용 false
                    } else {
                        Log.d("jiman=222", "Response body is null")
                        continuation.resume(null) // 작업 실패 시 false 반환
                    }
                } else {
                    Log.d("jiman=333", "Failure")
                    continuation.resume(null) // 작업 실패 시 false 반환
                }
            }

            override fun onFailure(call: Call<UserInterest>, t: Throwable) {
                Log.d("jiman=444", t.localizedMessage ?: "Unknown error")
                continuation.resume(null) // 작업 실패 시 false 반환
            }
        })
    }
}





;