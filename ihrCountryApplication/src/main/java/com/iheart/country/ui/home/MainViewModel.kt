package com.iheart.country.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iheart.country.data.Country
import com.iheart.country.repository.CountryDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToLong

@HiltViewModel
class MainViewModel @Inject constructor(private val countryDataRepository: CountryDataRepository) :
    ViewModel() {

    private val _countriesLiveData = MutableLiveData<List<Country>>()
    val countriesLiveData: LiveData<List<Country>> = _countriesLiveData

    val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    var job: Job? = null

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun loadData() {
        _loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = countryDataRepository.getAllCountries()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _countriesLiveData.postValue(response.body())
                    _loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }

    // -----------------------------------------------------
    // This function accepts the list of countries
    //
    // This function will find the country that has the largest number of borders
    //
    // This function returns Pair<Country.name, numberOfBorders as Int>
    // -----------------------------------------------------
    fun getTheCountryWithTheLargestNumberOfBorders(countries: List<Country>): Pair<String, Int> {
        lateinit var boarderCountMap: HashMap<String, Int>
        for (item in countries) {
            boarderCountMap[item.name] = if(item.borders != null) item.borders.size else 0
        }
        val maxBoarderCountry = boarderCountMap.maxBy { it.value }
        return Pair(maxBoarderCountry.key, maxBoarderCountry.value)
    }

    // -----------------------------------------------------
    // This function will find the top 5 countries that has the largest population per square mile (Density)
    //
    //  parameter : the list of countries
    //
    // Density = total population / area
    //
    //  density should be round off to nearest 1000th place
    //
    //  return List<Pair<Country.name, density as Float>
    // -----------------------------------------------------
    fun getTop5CountriesWithTheLargestDensity(countries: List<Country>): List<Pair<String, Float>> {
        lateinit var largestDensityCountryMap: HashMap<String, Long>
        for (item in countries) {
            val density =  item.populations / 20 //item.area
            val densityRoundOff = (density.toDouble() / 1000).roundToLong() *1000
            largestDensityCountryMap[item.name] = densityRoundOff
        }
        val sortedTopDensityCountry = largestDensityCountryMap.toSortedMap(compareByDescending { it })

        lateinit var topDensityCountryList : MutableList<Pair<String, Float>>

        if(sortedTopDensityCountry.size > 5) {
            for(index in 1..5 step 1) {
               // topDensityCountryList.add(Pair(sortedTopDensityCountry[index].key, item.value.toFloat()))
            }
        } else {
            for(item in sortedTopDensityCountry) {
                topDensityCountryList.add(Pair(item.key, item.value.toFloat()))
            }
        }

        return topDensityCountryList
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}
