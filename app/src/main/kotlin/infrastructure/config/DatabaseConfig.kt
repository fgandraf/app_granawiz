package infrastructure.config

import org.flywaydb.core.Flyway
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.AclEntry
import java.nio.file.attribute.AclEntryPermission
import java.nio.file.attribute.AclEntryType
import java.nio.file.attribute.AclFileAttributeView
import java.nio.file.attribute.PosixFilePermissions

object DatabaseConfig {

    fun runMigrations() {
        val flyway = Flyway.configure()
            .dataSource("jdbc:sqlite:${AppConfig.dbAbsolutePath}", null, null)
            .load()
        flyway.migrate()
        restrictDbPermissions()
    }

    private fun restrictDbPermissions() {
        val dbPath = Path.of(AppConfig.dbAbsolutePath)
        if (!Files.exists(dbPath)) return
        if (System.getProperty("os.name").lowercase().contains("win")) {
            applyWindowsAcl(dbPath)
        } else {
            runCatching {
                Files.setPosixFilePermissions(dbPath, PosixFilePermissions.fromString("rw-------"))
            }
        }
    }

    private fun applyWindowsAcl(dbPath: Path) {
        val aclView = Files.getFileAttributeView(dbPath, AclFileAttributeView::class.java) ?: return
        val owner = Files.getOwner(dbPath)
        val entry = AclEntry.newBuilder()
            .setType(AclEntryType.ALLOW)
            .setPrincipal(owner)
            .setPermissions(
                AclEntryPermission.READ_DATA,
                AclEntryPermission.WRITE_DATA,
                AclEntryPermission.READ_ATTRIBUTES,
                AclEntryPermission.WRITE_ATTRIBUTES,
                AclEntryPermission.SYNCHRONIZE,
            )
            .build()
        aclView.acl = listOf(entry)
    }
}
