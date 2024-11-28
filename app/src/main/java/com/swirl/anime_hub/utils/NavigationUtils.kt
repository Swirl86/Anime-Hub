package com.swirl.anime_hub.utils

import androidx.navigation.NavDestination
import com.swirl.anime_hub.ui.navigation.Screens

fun NavDestination?.getScreenTitle(): String {
    return this?.route?.let { myRoute ->
        Screens::class.sealedSubclasses
            .mapNotNull { it.objectInstance }
            .find { screen ->
                myRoute.startsWith(screen.route.substringBefore("{"))
            }?.title ?: "Anime Hub ++"
    } ?: "Anime Hub"
}