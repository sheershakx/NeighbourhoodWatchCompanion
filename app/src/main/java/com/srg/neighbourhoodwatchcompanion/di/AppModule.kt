package com.srg.neighbourhoodwatchcompanion.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.srg.neighbourhoodwatchcompanion.BuildConfig
import com.srg.neighbourhoodwatchcompanion.common.ImageCompressor
import com.srg.neighbourhoodwatchcompanion.data.repo.auth.AuthRepo
import com.srg.neighbourhoodwatchcompanion.data.repo.auth.AuthRepoImpl
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepo
import com.srg.neighbourhoodwatchcompanion.data.repo.incidents.IncidentRepoImpl
import com.srg.neighbourhoodwatchcompanion.data.repo.user.UserRepo
import com.srg.neighbourhoodwatchcompanion.data.repo.user.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val DATASTORE_NAME = "user_data_dt"

    @Singleton
    @Provides
    fun provideAuthRepo(supabaseAuth: Auth): AuthRepo {
        return AuthRepoImpl(supabaseAuth)
    }

    @Singleton
    @Provides
    fun provideUserRepo(postgrest: Postgrest, supabaseAuth: Auth): UserRepo {
        return UserRepoImpl(postgrest, supabaseAuth)
    }

    @Singleton
    @Provides
    fun provideIncidentRepo(
        postgrest: Postgrest,
        supabaseAuth: Auth,
        supabaseStorage: Storage
    ): IncidentRepo {
        return IncidentRepoImpl(postgrest, supabaseAuth, supabaseStorage)
    }

    @Singleton
    @Provides
    fun provideImageCompressor(@ApplicationContext context: Context): ImageCompressor {
        return ImageCompressor(context)
    }

    @Singleton
    @Provides
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        Places.initializeWithNewPlacesApiEnabled(context, BuildConfig.PLACES_KEY)
        return Places.createClient(context)
    }

    @Singleton
    @Provides
    fun provideDataStoreInstance(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(DATASTORE_NAME)
        }
    }
}

