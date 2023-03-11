package com.offmind.easyflashingcards

import com.offmind.easyflashingcards.data.repository.CardDataSource
import com.offmind.easyflashingcards.data.repository.CardsApiImpl
import com.offmind.easyflashingcards.domain.repository.CardsRepository
import com.offmind.easyflashingcards.domain.usecase.ReadCardsCsvUseCase
import com.offmind.easyflashingcards.presentation.viewmodel.CardFlashViewModel
import com.offmind.easyflashingcards.presentation.viewmodel.CardsListViewModel
import com.offmind.easyflashingcards.presentation.viewmodel.DecksViewModel
import com.offmind.easyflashingcards.presentation.viewmodel.HomeViewModel
import com.offmind.easyflashingcards.presentation.viewmodel.ImportCardsViewModel
import com.offmind.easyflashingcards.presentation.viewmodel.CardViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModules = module {
    single { ReadCardsCsvUseCase(context = androidContext()) }
    single {
        CardDataSource.create(context = androidContext())
    }
    single<CardsRepository> { CardsApiImpl(datasource = get(), ioDispatcher = Dispatchers.IO) }

    viewModelOf(::HomeViewModel)
    viewModelOf(::DecksViewModel)
    viewModelOf(::ImportCardsViewModel)
    viewModelOf(::CardsListViewModel)
    viewModelOf(::CardFlashViewModel)
    viewModelOf(::CardViewModel)
}
