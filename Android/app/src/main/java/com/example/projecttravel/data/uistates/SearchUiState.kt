package com.example.projecttravel.data.uistates

import com.example.projecttravel.model.SearchedPlace

data class SearchUiState(
    val searchedPlace: SearchedPlace? = null,
    val inOutChecker: String = "0",
    val inOutCheckerBoolean: Boolean = true,
)
