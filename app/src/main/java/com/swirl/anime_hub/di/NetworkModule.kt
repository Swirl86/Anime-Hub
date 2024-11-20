package com.swirl.anime_hub.di

import android.app.Application
import com.swirl.anime_hub.data.remote.JikanApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides an HttpLoggingInterceptor for logging network requests and responses.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        return loggingInterceptor
    }

    /**
     * Provides a RateLimitInterceptor for rate limiting network requests.
     */
    @Provides
    @Singleton
    fun provideRateLimitInterceptor(): RateLimitInterceptor {
        return RateLimitInterceptor(maxRequestsPerSecond = 3, maxRequestsPerMinute = 60)
    }

    /**
     * Provides an Interceptor to add custom headers to requests.
     */
    @Provides
    @Singleton
    fun provideCustomInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("Accept", "application/json")
                .build()
            chain.proceed(newRequest)
        }
    }

    /**
     * Provides an OkHttpClient with default headers and logging.
     * Ignore certificate errors (for development) due to using to old testing device.
     * Newer certificates (eg Let's Encrypt's certificate from ISRG Root X1) is not
     * supported by older Android versions (Android 7.1 or older).
     * */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        rateLimitInterceptor: RateLimitInterceptor,
        customInterceptor: Interceptor
    ): OkHttpClient {
        // TrustManager for handling SSL certificate issues (only for development use)
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(customInterceptor)
            .addInterceptor(rateLimitInterceptor)
            .build()
    }

    /**
     * Provides an OkHttpClient with default headers and logging.
     */
    /*@Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        rateLimitInterceptor: RateLimitInterceptor,
        customInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Log requests and responses
            .addInterceptor(customInterceptor)
            .addInterceptor(rateLimitInterceptor)
            .build()
    }*/

    /**
     * Provide a caching mechanism to reduce API calls and enhance performance.
     */
    @Provides
    @Singleton
    fun provideCache(context: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    /**
     * Provides Retrofit instance with cache enabled
     */
    @Provides
    @Singleton
    fun provideRetrofitWithCache(okHttpClient: OkHttpClient, cache: Cache): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.newBuilder().cache(cache).build()) // Add cache to OkHttpClient
            .build()
    }

    /**
     * Provides the ApiService for network requests
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): JikanApiService {
        return retrofit.create(JikanApiService::class.java)
    }
}