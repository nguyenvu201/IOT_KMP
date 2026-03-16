package caonguyen.vu

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform