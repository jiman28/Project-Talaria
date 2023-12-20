package com.example.projecttravel.ui.screens.auth.api

import android.util.Log
import com.example.projecttravel.data.uistates.UserPageUiState
import com.example.projecttravel.ui.viewmodels.UserPageViewModel
import com.example.projecttravel.data.RetrofitBuilderJson
import com.example.projecttravel.data.RetrofitBuilderString
import com.example.projecttravel.model.SendInterest
import com.example.projecttravel.model.SendSignIn
import com.example.projecttravel.model.User
import com.example.projecttravel.model.UserResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

/** ======================================================================================== */
/** asynchronous codes ===================================================================== */
/** function for getting Weather ====================*/
suspend fun loginApiCall(
    user: User,
    userPageUiState: UserPageUiState,
    userPageViewModel: UserPageViewModel,
): Boolean {
    return suspendCancellableCoroutine { continuation ->
        val call = RetrofitBuilderJson.travelJsonApiService.checkLogin(user)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>,
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        userPageViewModel.setUser(loginResponse)
                        Log.d("jiman=111", "Request Success + Response Success")
                        Log.d("jiman=111", call.toString())
                        Log.d("jiman=111", response.body().toString())
                        Log.d("jiman=111", userPageUiState.currentLogin.toString())
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

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("jiman=444", t.localizedMessage ?: "Unknown error")
                continuation.resume(false) // 작업 실패 시 false 반환
            }
        })
    }
}

suspend fun signInApiCall(
    sendSignIn: SendSignIn,
): String {
    return suspendCancellableCoroutine { continuation ->
        val call = RetrofitBuilderString.travelStringApiService.androidSignIn(
            email = sendSignIn.email,
            name = sendSignIn.name,
            password = sendSignIn.password
        )
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>,
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("jiman=111", "Request Success + Response Success")
                        Log.d("jiman=111", call.toString())
                        Log.d("jiman=111", response.body().toString())
                        // 원인을 알아낸것 같음 => 계속 null 값이 들어갔던 문제.
                        // 비동기 처리가 문제인것 같음. => loginApiCall  도 비동기 함수 내에서는 null 로 처리가 되지만 어쨋든 값은 정상적으로 들어옴.
                        // recomposition 이 문제인것 같음.
                        // 나중에 java쪽 서버도 스트링으로 다시바꾸고   이 함수 내에서 return 을 boolean 이 아닌 string 으로 바꾸면 될것같음.
                        continuation.resume(response.body().toString()) // 작업 성공 시 return 반환
//                        continuation.resume(false) // 오류 확인용 false
                    } else {
                        Log.d("jiman=222", "Response body is null")
                        continuation.resume("d") // 작업 실패 시 return 반환
                    }
                } else {
                    Log.d("jiman=333", "Failure")
                    continuation.resume("d") // 작업 실패 시 return 반환
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("jiman=444", t.localizedMessage ?: "Unknown error")
                continuation.resume("d") // 작업 실패 시 return 반환
            }
        })
    }
}

suspend fun interestSaveApiCall(
    sendInterest: SendInterest,
    userPageUiState: UserPageUiState,
): Boolean {
    return suspendCancellableCoroutine { continuation ->
        val call = RetrofitBuilderJson.travelJsonApiService.saveFirstInterest(sendInterest)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(
                call: Call<Boolean>,
                response: Response<Boolean>,
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("jiman=111", "Request Success + Response Success")
                        Log.d("jiman=111", call.toString())
                        Log.d("jiman=111", response.body().toString())
                        Log.d("jiman=111", userPageUiState.currentSignIn.toString())
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

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d("jiman=444", t.localizedMessage ?: "Unknown error")
                continuation.resume(false) // 작업 실패 시 false 반환
            }
        })
    }
}