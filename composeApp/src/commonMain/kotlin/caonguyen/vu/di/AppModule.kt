package caonguyen.vu.di

import caonguyen.vu.ui.auth.AuthViewModel
import caonguyen.vu.ui.dashboard.DashboardViewModel
import caonguyen.vu.bluetooth.BluetoothScanner
import caonguyen.vu.bluetooth.BluetoothDeviceController
import caonguyen.vu.bluetooth.FakeBluetoothScanner
import caonguyen.vu.bluetooth.FakeBluetoothDeviceController
import org.koin.dsl.module
import caonguyen.vu.shared.di.sharedModule

val appModule = module {
    factory { DashboardViewModel() }
    factory { AuthViewModel() }
    single<BluetoothScanner> { FakeBluetoothScanner() }
    single<BluetoothDeviceController> { FakeBluetoothDeviceController() }
}

val allModules = listOf(sharedModule, appModule)
