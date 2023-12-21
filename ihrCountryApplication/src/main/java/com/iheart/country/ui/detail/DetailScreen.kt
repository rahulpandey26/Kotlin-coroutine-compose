package com.iheart.country.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailScreen(viewModel: DetailViewModel) {

    val state = viewModel.uiState.collectAsState().value
    DetailScreenLayout(
        viewModel = viewModel,
        state = state
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailScreenLayout(
    viewModel: DetailViewModel,
    state: DetailViewUiState,
) {
    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                title = state.title,
            )
        }
    ) {

        Text(
            text = state.title,
            style = MaterialTheme.typography.h6,
        )

    }
}

@Composable
private fun TopBar(
    modifier: Modifier,
    title: String,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, color = MaterialTheme.colors.onSurface) },
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Preview
@Composable
private fun DetailScreenLayoutPreview() {
    DetailScreen(viewModel())
}
