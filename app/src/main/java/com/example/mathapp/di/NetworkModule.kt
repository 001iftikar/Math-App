package com.example.mathapp.di

import com.example.mathapp.utils.SupabaseConstants
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRealtimeDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideSupabase() : SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = SupabaseConstants.SUPABASE_URL,
            supabaseKey = SupabaseConstants.SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }

}