package caonguyen.vu

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import caonguyen.vu.di.allModules
import caonguyen.vu.ui.IoTDarkColors
import caonguyen.vu.ui.auth.LoginScreen
import caonguyen.vu.ui.dashboard.DashboardScreen
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(allModules)
    }) {
        MaterialTheme(colorScheme = IoTDarkColors) {
            Navigator(DashboardScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}