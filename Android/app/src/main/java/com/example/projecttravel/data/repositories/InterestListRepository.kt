package com.example.projecttravel.data.repositories

import com.example.projecttravel.model.InterestInfo
import com.example.projecttravel.network.TravelApiService

/**
 * Repository retrieves Country data from underlying data source.
 */
interface InterestListRepository {
    /** Retrieves list of amphibians from underlying data source */
    suspend fun getInterestList(): List<InterestInfo>
}

/**
 * Network Implementation of repository that retrieves Country data from underlying data source.
 */
class DefaultInterestListRepository(
    private val travelApiService: TravelApiService
) : InterestListRepository {
    /** Retrieves list of Country from underlying data source */
    override suspend fun getInterestList(): List<InterestInfo> = travelApiService.getInterestList()
}