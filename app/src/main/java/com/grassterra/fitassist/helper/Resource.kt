package com.grassterra.fitassist.helper

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val errorMessage: String) : Resource<Nothing>()
}