package com.iheart.country.ui.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import com.iheart.country.data.Country
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailComposeActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        val view = ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DetailScreen(viewModel)
            }
        }

        setContentView(view)
    }

    private fun init() {
        val country = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(COUNTRY_DATA, Country::class.java)
        } else {
            intent.getParcelableExtra<Country>(COUNTRY_DATA)
        }

        country?.let {
            this@DetailComposeActivity.title = it.name
            viewModel.updateUiState(title = country.name)
            viewModel.setImage(countryCode = country.alpha2Code)
            viewModel.updateUiState(nativeName = country.name)
            if (country.borders != null) viewModel.updateUiState(boarderCountries = country.borders)
            viewModel.updateUiState(countryLanguages = country.languages)
        }

        /*  intent.getStringExtra(COUNTRY_CODE)?.let { countryCode ->
              viewModel.setImage(countryCode = countryCode)
          }*/

        lifecycleScope.launchWhenStarted {
            viewModel.navigationEvents.collect { navigationEvent ->
                when (navigationEvent) {
                    NavigationEvent.NavigateBack -> {
                        onBackPressed()
                    }
                }
            }
        }

    }

}
