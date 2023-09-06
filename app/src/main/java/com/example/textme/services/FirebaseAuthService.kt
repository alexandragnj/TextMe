package com.example.textme.services

import com.example.textme.presentation.auth.Result

interface FirebaseAuthService {
    suspend fun login(email: String, password: String): Result<Exception, Unit>
    suspend fun register(email: String, password: String): Result<Exception, Unit>
}