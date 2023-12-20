package com.example.projecttravel.data.repositories

import com.example.projecttravel.model.TourAttractionSearchInfo
import com.example.projecttravel.network.TravelApiService

/**
 * Repository retrieves Country data from underlying data source.
 */
interface TourAttrSearchListRepository {
    /** Retrieves list of amphibians from underlying data source */
    suspend fun getTourAttrSearchList(): List<TourAttractionSearchInfo>
}

/**
 * Network Implementation of repository that retrieves Country data from underlying data source.
 */
class DefaultTourAttrSearchListRepository(
    private val travelApiService: TravelApiService
) : TourAttrSearchListRepository {
    /** Retrieves list of Country from underlying data source */
    override suspend fun getTourAttrSearchList(): List<TourAttractionSearchInfo> = travelApiService.getTourAttrSearchList()
}