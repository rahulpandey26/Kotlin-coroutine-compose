package com.iheart.country.ui.detail

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.iheart.country.R
import com.iheart.country.data.Country
import com.iheart.country.databinding.DetailViewBinding
import com.iheart.country.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<DetailViewModel, DetailViewBinding>() {

    override val viewModel: DetailViewModel by viewModels()

    override fun initViewBinding(): DetailViewBinding = DetailViewBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initView()
    }

    private fun initView() {
        setContentView(viewBinding.root)

        val country = getCountry()
        with(viewBinding) {
            title.text = viewModel.uiState.value.title

            country?.let {
                tvNativeName.text = country.nativeName
                setCountryBorderSection(country)
                setCountryLanguage(country)
            }

            tvBack.setOnClickListener { this@DetailActivity.finish() }
        }
    }

    private fun DetailViewBinding.setCountryBorderSection(country: Country) {
        tvBoarderCountryCount.text =
            getString(R.string.border_section_title, country.borders?.size.toString())
        if (country.borders != null) {
            for (item in country.borders) {
                val dynamicTextView = TextView(this@DetailActivity)
                dynamicTextView.text = item
                lytBoarderCountry.addView(dynamicTextView)
            }
        }
    }

    private fun DetailViewBinding.setCountryLanguage(country: Country) {
        tvOfficialLanguage.text = getString(R.string.language_section_title)
        for (item in country.languages) {
            val dynamicTextView = TextView(this@DetailActivity)
            dynamicTextView.text = "${item.name}::${item.nativeName}"
            lytCountryLanguage.addView(dynamicTextView)
        }
    }

    private fun init() {
        val country = getCountry()

        country?.let {
            this@DetailActivity.title = it.name
            viewModel.updateUiState(title = country.name)
            viewModel.setImage(countryCode = country.alpha2Code)
            viewModel.updateUiState(nativeName = country.name)
        }

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

    private fun getCountry() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(COUNTRY_DATA, Country::class.java)
    } else {
        intent.getParcelableExtra(COUNTRY_DATA)
    }

}
