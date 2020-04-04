import di.applicationModule
import kotlinx.serialization.UnstableDefault
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


@UnstableDefault
fun main() {
    startKoin {
        printLogger(level = Level.NONE)
        modules(applicationModule)
    }
    Application().initialMenu()
}
