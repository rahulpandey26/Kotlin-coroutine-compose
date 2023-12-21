package com.iheart.country.api

import com.iheart.country.data.Country
import retrofit2.Response
import javax.inject.Inject

class CountriesApi @Inject constructor(private val countriesService: CountriesService) {

     suspend fun getAllCountries(): Response<List<Country>> = countriesService.getAll()
}
