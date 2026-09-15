package com.tuusuario.smartclock.di

import android.content.Context
import com.tuusuario.smartclock.core.audio.TtsManager
import com.tuusuario.smartclock.core.audio.VolumeManager
import com.tuusuario.smartclock.data.preferences.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): SettingsDataStore {
        return SettingsDataStore(context)
    }

    @Provides
    @Singleton
    fun provideTtsManager(
        @ApplicationContext context: Context
    ): TtsManager {
        return TtsManager(context)
    }

    @Provides
    @Singleton
    fun provideVolumeManager(
        @ApplicationContext context: Context
    ): VolumeManager {
        return VolumeManager(context)
    }
}
