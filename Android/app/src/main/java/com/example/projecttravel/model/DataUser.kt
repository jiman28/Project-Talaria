package com.example.projecttravel.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val email: String,
    val password: String,
)

@Serializable
data class SendSignIn (
    val email: String,
    val name: String,
    val password: String,
)

@Serializable
data class UserResponse (
    val id: String,
    val email: String,
    val name: String,
    val picture: String?,
)

@Serializable
data class UserInterest (
    val id: String,
    val user: String,
    val reliability: String,
    val sights: String,
    val nature: String,
    val culture: String,
    val history: String,
    val food: String,
    val religion: String,
)

@Serializable
data class SendInterest (
    val email: String,
    val sights: String,
    val nature: String,
    val culture: String,
    val history: String,
    val food: String,
    val religion: String,
)

@Serializable
data class CheckOtherUserById (
    val id: String,
)

@Serializable
data class UserRadarChartInfo (
    val sights: Float,
    val nature: Float,
    val culture: Float,
    val history: Float,
    val food: Float,
    val religion: Float,
)
