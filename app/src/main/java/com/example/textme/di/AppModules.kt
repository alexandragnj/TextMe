package com.example.textme.di

import com.example.textme.presentation.auth.AuthViewModel
import com.example.textme.services.FirebaseAuthService
import com.example.textme.services.FirebaseAuthServiceImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<FirebaseAuthService> { FirebaseAuthServiceImpl() }
    viewModel {
        AuthViewModel(authService = get())
    }
}