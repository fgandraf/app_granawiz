package config

import org.flywaydb.core.Flyway

object DatabaseConfig {

    fun runMigrations() {
        val flyway = Flyway.configure()
            .dataSource("jdbc:sqlite:money.db", null, null)
            .load()
        flyway.migrate()
    }

}
