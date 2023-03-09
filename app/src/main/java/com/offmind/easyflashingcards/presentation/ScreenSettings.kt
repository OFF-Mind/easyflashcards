package com.offmind.easyflashingcards.presentation

data class ScreenSettings(
    val title: String,
    val showHomeButton: Boolean = false,
    val fabButtonClick: (()->Unit)? = null
)

val Empty: ScreenSettings =
    ScreenSettings(
        title = "",
        showHomeButton = false,
        fabButtonClick = null)
