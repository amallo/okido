package com.babatunde.okido.core.providers

interface OnboardingStore {
    fun isCompleted(): Boolean
    fun complete()
}
