package com.babatunde.okido.ui

import androidx.lifecycle.ViewModel
import com.babatunde.okido.core.GetGreeting

class GreetingViewModel(getGreeting: GetGreeting) : ViewModel() {
    val greeting: String = getGreeting()
}
