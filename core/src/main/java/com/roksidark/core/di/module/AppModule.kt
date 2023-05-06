package com.roksidark.core.di.module

import com.roksidark.core.data.interceptor.UserAgentHeaderInterceptor
import com.roksidark.core.data.repository.RemoteRepositoryImpl
import com.roksidark.core.domain.network.MusicBrainzApi
import com.roksidark.core.domain.repository.RemoteRepository
import com.roksidark.core.domain.usecase.ArtistUseCases
import com.roksidark.core.domain.usecase.GetAlbumsRemotely
import com.roksidark.core.domain.usecase.GetArtistsRemotely
import com.roksidark.core.util.getUserAgent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }



    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(UserAgentHeaderInterceptor(getUserAgent()))
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(com.roksidark.core.util.Constant.BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MusicBrainzApi =
        retrofit.create(MusicBrainzApi::class.java)

    @Singleton
    @Provides
    fun providesRepository(musicBrainzApi: MusicBrainzApi): RemoteRepository =
        RemoteRepositoryImpl(musicBrainzApi)

    @Singleton
    @Provides
    fun provideArtistsUseCases(remoteRepository: RemoteRepository
    ): ArtistUseCases {
        return ArtistUseCases(
            getArtistsRemotely = GetArtistsRemotely(remoteRepository),
            getAlbumRemotely = GetAlbumsRemotely(remoteRepository)
        )
    }
}