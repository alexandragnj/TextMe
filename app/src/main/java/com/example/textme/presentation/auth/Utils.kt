package com.example.textme.presentation.auth

fun assertFieldsEmpty(vararg fields: String): Boolean {
    fields.find { it.isEmpty() || it.isBlank()}?.let { return false } ?: return true
}