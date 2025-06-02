package com.srg.neighbourhoodwatchcompanion.data.repo.auth

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import javax.inject.Inject


interface AuthRepo {
    suspend fun loginUser(userEmail: String, userPassword: String)
    suspend fun registerUser(email: String, password: String)

}

class AuthRepoImpl @Inject constructor(
    private val supabaseAuth: Auth
) : AuthRepo {
    override suspend fun loginUser(userEmail: String, userPassword: String) {
        supabaseAuth.signInWith(Email) {
            email = userEmail
            password = userPassword
        }

    }

    override suspend fun registerUser(email: String, password: String) {
        TODO("Not yet implemented")
    }

}