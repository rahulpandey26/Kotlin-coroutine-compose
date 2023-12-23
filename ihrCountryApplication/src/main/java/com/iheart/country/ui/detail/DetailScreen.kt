package com.iheart.country.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iheart.country.R
import com.iheart.country.data.Language

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBackPressed: () -> Unit,
) {
    val state = viewModel.uiState.collectAsState().value
    FullScreen(
        state = state,
        onBackPressed = onBackPressed
    )
}

@Composable
private fun FullScreen(
    state: DetailViewUiState,
    onBackPressed: () -> Unit,
) {
    Column {
        TopBar(
            modifier = Modifier.fillMaxWidth(),
            title = state.title,
            onBackPressed = onBackPressed
        )
        ScreenBody(state = state)
    }
}

@Composable
private fun ScreenBody(
    state: DetailViewUiState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row {
            state.flagImage?.asImageBitmap()?.let {
                Image(
                    modifier = Modifier
                        .size(width = 100.dp, height = 70.dp),
                    contentScale = ContentScale.FillBounds,
                    bitmap = it,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    text = state.title
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    text = stringResource(R.string.native_name_title),
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    text = state.nativeName,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp,
            text = stringResource(R.string.border_section_title, state.boarderCountries.size),
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp,
            text = state.boarderCountries.joinToString(separator = "\n"),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp,
            text = stringResource(R.string.language_section_title),
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp,
            text = state.countryLanguages.joinToString(separator = "\n") { language -> language.name + "::" + language.nativeName })
    }
}

@Composable
private fun TopBar(
    modifier: Modifier,
    title: String,
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, color = MaterialTheme.colors.onSurface) },
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
@Preview
fun FullScreenPreview() {
    val state1 = DetailViewUiState(
        boarderCountries = listOf("x", "y", "z"),
        countryLanguages = listOf(
            Language("language1", "nName", "ss", "ss"),
            Language("language2", "nName", "ss", "ss"),
            Language("language3", "nName", "ss", "ss")
        ),
    )
    FullScreen(
        state = state1,
        onBackPressed = {}
    )
}