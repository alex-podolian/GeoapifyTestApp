package com.example.geoapifytestapp.utils

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}