package com.iheart.country.ui.detail

import android.graphics.Bitmap
import com.iheart.country.data.Language

data class DetailViewUiState(
    val title: String = "default title",
    val flagImage: Bitmap? = null,
    val nativeName: String = "default nativeName",
    val boarderCountries: List<String> = emptyList(),
    val countryLanguages: List<Language> = emptyList()
)

sealed class NavigationEvent {
    object NavigateBack : NavigationEvent()
}

const val COUNTRY_NAME = "COUNTRY_NAME"
const val COUNTRY_CODE = "COUNTRY_CODE"
const val NATIVE_NAME = "NATIVE_NAME"
const val COUNTRY_DATA = "countryObjKey"
