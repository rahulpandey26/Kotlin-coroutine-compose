package com.iheart.country.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.iheart.country.data.Country
import com.iheart.country.databinding.ActivityMainBinding
import com.iheart.country.ui.base.BaseActivity
import com.iheart.country.ui.detail.COUNTRY_DATA
import com.iheart.country.ui.detail.DetailActivity
import com.iheart.country.ui.detail.DetailComposeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel: MainViewModel by viewModels()

    override fun initViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        observeViewModel()

        viewModel.loadData()
    }

    private fun observeViewModel() {
        with(viewModel) {
            countriesLiveData.observe(this@MainActivity, ::onCountriesListLoaded)
            errorMessage.observe(this@MainActivity) {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
            }
            loading.observe(this@MainActivity) {
                if (it) {
                    viewBinding.progressBar.visibility = View.VISIBLE
                } else {
                    viewBinding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun onCountriesListLoaded(data: List<Country>) = with(viewBinding) {
        if(data.isEmpty()) {
            nullTextView.visibility = View.VISIBLE
            gridview.visibility = View.GONE
        } else {
            nullTextView.visibility = View.GONE
            gridview.visibility = View.VISIBLE

            val countryGridViewAdapter = CountryGridviewAdapter(this@MainActivity, data)
            gridview.adapter = countryGridViewAdapter
            gridview.setOnItemClickListener { adapterView, view, i, l ->
                onItemClicked(data[i])
            }
        }
    }

    private fun onItemClicked(country: Country) {
        // show DetailActivity - xml view
        // or
        // show DetailComposeActivity - compose view
        startActivity(Intent(this@MainActivity, DetailComposeActivity::class.java).apply {
            putExtra(COUNTRY_DATA, country)
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        viewBinding.gridview.numColumns =
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 1
        super.onConfigurationChanged(newConfig)
    }
}
