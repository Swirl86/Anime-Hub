package com.swirl.anime_hub.ui.navigation

import android.util.Log
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: Screens.AnimeList.route

    val showBackArrow = remember(currentRoute) {
        currentRoute != Screens.AnimeList.route
    }

    /*
    // For testing navigation
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.i("NavigationListener", "Navigated to: ${destination.route}")
        }
    }*/

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavDrawer(
                route = currentRoute,
                navController = navController,
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        },
        gesturesEnabled = true,
        content = {
            Scaffold(
                topBar = { TopBar(navController, showBackArrow, drawerState) },
                content = { paddingValues ->
                    NavGraph(navController, paddingValues)
                }
            )
        }
    )
}
