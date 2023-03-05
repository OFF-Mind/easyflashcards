package com.offmind.easyflashingcards.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : BaseViewModel() {

    private val _state: MutableStateFlow<HomeViewModelState> = MutableStateFlow(HomeViewModelState.Idle)
    val state: StateFlow<HomeViewModelState> = _state

    sealed class HomeViewModelState : BaseViewModelState(title = "Easy Flash Cards") {
        object Idle: HomeViewModelState()
    }

}