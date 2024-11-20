package com.swirl.anime_hub.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CacheControlInterceptorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CustomInterceptorQualifier