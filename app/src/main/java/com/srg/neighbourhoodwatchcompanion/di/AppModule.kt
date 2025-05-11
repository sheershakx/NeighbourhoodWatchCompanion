package com.srg.neighbourhoodwatchcompanion.di

import android.content.Context
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
class AppModule {
    @Singleton
    @Provides
    fun provideAuthRepo(supabaseAuth: Auth): AuthRepo {
        return AuthRepoImpl(supabaseAuth)
    }

    @Singleton
    @Provides
    fun provideUserRepo(postgrest: Postgrest,supabaseAuth: Auth): UserRepo {
        return UserRepoImpl(postgrest,supabaseAuth)
    }

    @Singleton
    @Provides
    fun provideIncidentRepo(postgrest: Postgrest,supabaseAuth: Auth,supabaseStorage: Storage): IncidentRepo{
        return IncidentRepoImpl(postgrest,supabaseAuth,supabaseStorage)
    }

    @Singleton
    @Provides
    fun provideImageCompressor(@ApplicationContext context: Context): ImageCompressor{
        return ImageCompressor(context)

    }
}