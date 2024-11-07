package config

import org.flywaydb.core.Flyway

object DatabaseConfig {

    fun runMigrations() {
        val flyway = Flyway.configure()
            .dataSource("jdbc:sqlite:money_map.db", null, null)
            .load()
        flyway.migrate()
    }

}
