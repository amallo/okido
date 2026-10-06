package com.babatunde.okido.core.providers

interface ParentPin {
    fun verify(pin: String): Boolean
    fun isSet(): Boolean
    fun save(pin: String)
}
