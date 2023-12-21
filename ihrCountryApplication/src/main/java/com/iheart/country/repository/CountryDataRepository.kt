package com.iheart.country.repository

import com.iheart.country.api.CountriesApi
import com.iheart.country.api.CountriesService
import com.iheart.country.data.Country
import retrofit2.Response
import javax.inject.Inject

class CountryDataRepository @Inject constructor(private val countriesApi: CountriesApi) {

    suspend fun getAllCountries(): Response<List<Country>> =
        countriesApi.getAllCountries()

    fun getImageUrl(): String = "${CountriesService.BASE_URL}flag/png250px/us.png"

}
