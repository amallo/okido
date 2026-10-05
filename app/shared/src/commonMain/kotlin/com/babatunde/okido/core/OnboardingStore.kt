package com.babatunde.okido.core

interface OnboardingStore {
    fun isCompleted(): Boolean
    fun complete()
}
