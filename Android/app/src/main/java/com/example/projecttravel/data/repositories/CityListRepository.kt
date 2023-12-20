package com.example.projecttravel.data.repositories

import com.example.projecttravel.model.CityInfo
import com.example.projecttravel.network.TravelApiService

/**
 * Repository retrieves Country data from underlying data source.
 */
interface CityListRepository {
    /** Retrieves list of amphibians from underlying data source */
    suspend fun getCityList(): List<CityInfo>
}

/**
 * Network Implementation of repository that retrieves Country data from underlying data source.
 */
class DefaultCityListRepository(
    private val travelApiService: TravelApiService
) : CityListRepository {
    /** Retrieves list of Country from underlying data source */
    override suspend fun getCityList(): List<CityInfo> = travelApiService.getCityList()
}
