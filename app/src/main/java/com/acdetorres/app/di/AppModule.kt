package com.acdetorres.app.di

import com.acdetorres.app.di.network.ApiService
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


//In here are the modules. Only 1 module is used here that provides the retrofit and ApiService
@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    //Provides Retrofit networking library with logging
    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        val okHttpClient = OkHttpClient.Builder()

        //LOGGER
        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient.addInterceptor(loggingInterceptor)

        //Plugin Logger for easier view on JSON responses and raw responses.
        okHttpClient.addInterceptor(OkHttpProfilerInterceptor())

        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



    //Provides ApiService to Repositories
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }


}