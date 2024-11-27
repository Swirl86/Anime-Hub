package com.swirl.anime_hub.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.swirl.anime_hub.R

@Composable
fun NavDrawer(
    route: String,
    navController: NavController,
    closeDrawer: () -> Unit = {}
) {
    ModalDrawerSheet (
        modifier = Modifier.width(240.dp).fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_app_icon),
                contentDescription = "App Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .padding(bottom = 8.dp)
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "Anime Hub",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.5.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        /* Items in the drawer */
        Column(
            modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth()
        ) {
            NavigationDrawerItem(
                label = { Text(Screens.AnimeList.title) },
                selected = route == Screens.AnimeList.route,
                onClick = {
                    closeDrawer()
                    NavigationActions.navigateToAnimeList(navController)
                },
                icon = {
                    Icon(imageVector = Screens.AnimeList.icon, contentDescription = "Home Icon")
                },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
            /* TODO implement */
            NavigationDrawerItem(
                label = { Text("Top Anime") },
                selected = false,
                onClick = { /* Handle Top Anime navigation */ },
                icon = {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Star Icon")
                }

            )
            NavigationDrawerItem(
                label = { Text("Manga") },
                selected = false,
                onClick = { /* Handle Manga navigation */ },
                icon = {
                    Icon(imageVector = Icons.Default.Book, contentDescription = "Book Icon")
                }
            )
        }
    }
}