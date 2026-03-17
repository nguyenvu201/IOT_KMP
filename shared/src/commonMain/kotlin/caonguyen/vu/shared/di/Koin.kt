package caonguyen.vu.shared.di

import org.koin.core.context.startKoin
import org.koin.dsl.module

val sharedModule = module {
    // Shared dependencies like repositories will be defined here
}

fun initKoin() {
    startKoin {
        modules(sharedModule)
    }
}
