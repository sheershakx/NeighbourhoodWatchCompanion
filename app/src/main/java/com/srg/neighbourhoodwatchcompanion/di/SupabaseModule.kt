package com.srg.neighbourhoodwatchcompanion.di

import com.srg.neighbourhoodwatchcompanion.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SupabaseModule {

    @Singleton
    @Provides
    fun supabase(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            //install other modules
        }
    }

    @Singleton
    @Provides
    fun provideSupabaseAuth(supabase: SupabaseClient): Auth {
        return supabase.auth
    }

    @Singleton
    @Provides
    fun provideSupabasePostgrest(supabase: SupabaseClient): Postgrest {
        return supabase.postgrest
    }
}