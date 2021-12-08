package com.dony.dramatest.di

import android.content.Context
import android.content.pm.ApplicationInfo
import com.dony.dramatest.data.local.dao.UserDao
import com.dony.dramatest.data.remote.GithubApiConstants
import com.dony.dramatest.data.remote.GithubApiService
import com.dony.dramatest.data.repository.SearchRepository
import com.dony.dramatest.data.repository.SearchRepositoryImpl
import com.dony.dramatest.data.source.SearchDataSource
import com.dony.dramatest.data.source.SearchDataSourceImpl
import com.dony.dramatest.util.DateDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * (API, DataSource, Repository) Remote DI
 */

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    /**
     * Retrofit + OkHttp
     */

    @Provides
    @Singleton
    fun providesRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GithubApiConstants.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer()).create())
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        with(OkHttpClient().newBuilder()) {
            cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong()))
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            addInterceptor {
                it.proceed(it.request().newBuilder().addHeader("Authorization", "KakaoAK 0db5e4ebca76e3078cdb309367f39e0d").build())
            }
            context.let {
                // 디버깅 모드에서만 Http 로그를 찍는다.
                if (0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                    addInterceptor(httpLoggingInterceptor)
                }
            }
            return build()
        }
    }

    @Provides
    @Singleton
    fun providesRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): GithubApiService {
        return retrofit.create(GithubApiService::class.java)
    }

    /**
     * DataSource
     */

    @Provides
    @Singleton
    fun provideDocumentsDataSource(
        githubApiService: GithubApiService,
        userDao: UserDao
    ): SearchDataSource {
        return SearchDataSourceImpl(githubApiService, userDao)
    }

    /**
     * Repository
     */

    @Provides
    @Singleton
    fun provideDocumentsRepository(
        dataSource: SearchDataSource
    ): SearchRepository {
        return SearchRepositoryImpl(dataSource)
    }
}