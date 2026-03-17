package caonguyen.vu.di

import caonguyen.vu.ui.dashboard.DashboardViewModel
import org.koin.dsl.module
import caonguyen.vu.shared.di.sharedModule

val appModule = module {
    factory { DashboardViewModel() }
}

val allModules = listOf(sharedModule, appModule)
