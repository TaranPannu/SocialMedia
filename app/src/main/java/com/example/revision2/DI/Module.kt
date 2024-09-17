package com.example.revision2.DI


import android.content.Context
import android.content.SharedPreferences

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Module
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@InstallIn(SingletonComponent::class)// module only exist till the application excist once the application finish, module will also be destroyed
@Module
class Module
{
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences
    {
        return context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideMistakeDatabase(@ApplicationContext context: Context): MistakeDatabase
    {
        return MistakeDatabase.invoke(context)
    }

    @Provides
    fun provideMistakeDao(database: MistakeDatabase): Dao
    {
        return database.getMistakeDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient
    {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val token = sharedPreferences.getString("jwt_token", null)
                val newRequest = if (token != null) {
                    request.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    request
                }
                chain.proceed(newRequest)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit
    {
        return Retrofit.Builder()
            .baseUrl("http://-----)
            .client(okHttpClient) // Add the OkHttpClient to the Retrofit instance
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitServices(retrofit: Retrofit): RetrofitServices
    {
        return retrofit.create(RetrofitServices::class.java)
    }

}
