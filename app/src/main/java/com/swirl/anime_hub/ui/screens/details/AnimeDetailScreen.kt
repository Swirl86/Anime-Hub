package com.swirl.anime_hub.ui.screens.details

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.swirl.anime_hub.ui.components.InfoRow
import com.swirl.anime_hub.ui.screens.common.LoadingScreen
import com.swirl.anime_hub.utils.extension.isNullOrEmptyString
import com.swirl.anime_hub.utils.extension.toFormattedDate
import com.swirl.anime_hub.viewmodel.AnimeViewModel


@Composable
fun AnimeDetailScreen(
    animeId: Int,
    viewModel: AnimeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val animeDetails by viewModel.animeDetails.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    LaunchedEffect(animeId) {
        viewModel.getAnimeDetails(animeId)
    }

    if (animeDetails == null) { LoadingScreen() }


    animeDetails?.let { details ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = details.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(details.images.jpg.imageUrl)
                        .crossfade(true)
                        .build()
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop,
                contentDescription = details.title
            )

            InfoRow(label = "Episodes:", value = details.episodes?.toString() ?: "N/A")
            InfoRow(label = "Score:", value = details.score?.toString() ?: "N/A")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Synopsis:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = details.synopsis ?: "N/A",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            details.startDate?.isNullOrEmptyString()?.let {
                InfoRow(label = "Start Date:", value = it.toFormattedDate())
            }
            details.endDate?.isNullOrEmptyString()?.let {
                InfoRow(label = "End Date:", value = it.toFormattedDate())
            }

            Spacer(modifier = Modifier.height(16.dp))

            details.trailer?.url?.isNullOrEmptyString()?.let {
                Text(
                    text = "Watch Trailer",
                    color = Color.Blue,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "Failed to open the link", Toast.LENGTH_SHORT).show()
                            }
                        }
                )
            }

            details.genres?.let {
                Text(
                    text = "Genres:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = it.joinToString { genre -> genre.name },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            details.background?.isNullOrEmptyString()?.let {
                Text(
                    text = "Background:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }

    errorState?.let {
        Text(
            text = "Error: ${it.message}",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    }
}