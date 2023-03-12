package com.offmind.easyflashingcards.presentation

data class ScreenSettings(
    val title: String,
    val showHomeButton: Boolean = false,
    val homeButtonAction: (()->Unit)? = null,
    val fabButtonClick: (() -> Unit)? = null,
    val actions: List<TopBarAction> = emptyList()
)

data class TopBarAction(
    val icon: Int,
    val action: ()->Unit
)

val Empty: ScreenSettings =
    ScreenSettings(
        title = "",
        showHomeButton = false,
        fabButtonClick = null
    )
