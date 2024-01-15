package com.vyhorelov.android.bookassistantapp

import android.app.Application

class BookAssistantApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BookRepository.initialize(this)
    }
}