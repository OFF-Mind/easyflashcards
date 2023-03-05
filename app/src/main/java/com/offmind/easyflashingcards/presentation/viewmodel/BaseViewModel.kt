package com.offmind.easyflashingcards.presentation.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    sealed class ViewModelEvent {
        class ImportCards(toDeckId: Int, pathToFile: String) : ViewModelEvent()
    }

    sealed class BaseViewModelState(open val title: String)

    override fun onCleared() {
        println("Viewmodel cleared: ${javaClass.name}")
        super.onCleared()
    }
}