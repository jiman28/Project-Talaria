package com.example.projecttravel.data.repositories

import com.example.projecttravel.model.TourAttractionInfo
import com.example.projecttravel.network.TravelApiService

/**
 * Repository retrieves Country data from underlying data source.
 */
interface TourAttractionListRepository {
    /** Retrieves list of amphibians from underlying data source */
    suspend fun getTourAttractionList(): List<TourAttractionInfo>
}

/**
 * Network Implementation of repository that retrieves Country data from underlying data source.
 */
class DefaultTourAttractionListRepository(
    private val travelApiService: TravelApiService
) : TourAttractionListRepository {
    /** Retrieves list of Country from underlying data source */
    override suspend fun getTourAttractionList(): List<TourAttractionInfo> = travelApiService.getTourAttractionList()
}