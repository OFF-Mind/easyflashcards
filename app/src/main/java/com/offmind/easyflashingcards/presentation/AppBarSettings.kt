package com.offmind.easyflashingcards.presentation

data class AppBarSettings(
    val title: String,
    val showHomeButton: Boolean
)

val Empty: AppBarSettings =
    AppBarSettings(title = "", showHomeButton = false)
