package infrastructure.config

import domain.entity.*
import domain.entity.UserPreference
import domain.entity.account.BankAccount
import domain.entity.account.CheckingAccount
import domain.entity.account.CreditCardAccount
import domain.entity.account.SavingsAccount
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.util.*

object HibernateUtil {
    private val sessionFactory: SessionFactory

    init {
        val configuration = Configuration()

        // Registro das classes de entidade
        configuration.addAnnotatedClass(Group::class.java)
        configuration.addAnnotatedClass(Tag::class.java)
        configuration.addAnnotatedClass(Party::class.java)
        configuration.addAnnotatedClass(PartyName::class.java)
        configuration.addAnnotatedClass(Transaction::class.java)
        configuration.addAnnotatedClass(Schedule::class.java)
        configuration.addAnnotatedClass(Category::class.java)
        configuration.addAnnotatedClass(Subcategory::class.java)
        configuration.addAnnotatedClass(BankAccount::class.java)
        configuration.addAnnotatedClass(CheckingAccount::class.java)
        configuration.addAnnotatedClass(CreditCardAccount::class.java)
        configuration.addAnnotatedClass(SavingsAccount::class.java)
        configuration.addAnnotatedClass(UserPreference::class.java)

        // Propriedades do Hibernate
        val settings = Properties()
        settings["hibernate.connection.driver_class"] = "org.sqlite.JDBC"
        settings["hibernate.connection.url"] = "jdbc:sqlite:${AppConfig.dbAbsolutePath}?foreign_keys=on"
        settings["hibernate.dialect"] = "org.hibernate.community.dialect.SQLiteDialect"
        settings["hibernate.jdbc.time_zone"] = "UTC"
        settings["hibernate.show_sql"] = "false"
        settings["hibernate.format_sql"] = "false"
        settings["hibernate.hbm2ddl.auto"] = "validate"

        configuration.addProperties(settings)

        sessionFactory = configuration.buildSessionFactory()
    }

    fun getSessionFactory(): SessionFactory {
        return sessionFactory
    }
}
