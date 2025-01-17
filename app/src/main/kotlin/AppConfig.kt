import java.io.File
import java.nio.file.Paths

object AppConfig {
    private val dbDirectory = Paths
        .get(System.getProperty("user.home"), ".granawiz", "database")
        .toFile()
        .apply { this.mkdirs() }

    val dbAbsolutePath: String = File(dbDirectory, "granawiz.db").absolutePath
}