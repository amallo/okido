package com.babatunde.okido

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform