package com.iheart.country.ui.detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iheart.country.data.Country
import com.iheart.country.data.Language
import com.iheart.country.repository.CountryDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(private val countryDataRepository: CountryDataRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(DetailViewUiState())
    val uiState: StateFlow<DetailViewUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    fun setImage(countryCode: String) {
        viewModelScope.launch(Dispatchers.IO)  {
            try {

                // Image name *countryCode".png
                // ex) https://ihrandroidtesting.s3.amazonaws.com/country/flag/png250px/af.png
                // ex) https://ihrandroidtesting.s3.amazonaws.com/country/flag/png250px/us.png
                val url = URL(countryDataRepository.getImageUrl())
                val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())

                updateUiState(flagImage = image)
            } catch (e: IOException) {
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateBack)
        }
    }


    // -----------------------------------------------------
    //
    //   when the user select the Border country
    //      it will open new DetailActivity / DetailComposeActivity for the selected country
    //
    //  parameter : 3 alpha code for the country
    //
    // -----------------------------------------------------
    fun handleBorderCountrySelected(alpha3Code: String) {



    }

    fun updateUiState(
        title: String = _uiState.value.title,
        flagImage: Bitmap? = _uiState.value.flagImage,
        nativeName: String = _uiState.value.nativeName,
        boarderCountries: List<String> = _uiState.value.boarderCountries,
        countryLanguages: List<Language> = _uiState.value.countryLanguages
    ) {
        _uiState.value = _uiState.value.copy(
            title = title,
            flagImage = flagImage,
            nativeName = nativeName,
            boarderCountries = boarderCountries,
            countryLanguages = countryLanguages
        )
    }
}
