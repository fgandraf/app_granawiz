package config

import model.entity.AssociatedReceiverName
import model.entity.Group
import model.entity.Receiver
import model.entity.Tag
import model.entity.account.*
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
        configuration.addAnnotatedClass(Receiver::class.java)
        configuration.addAnnotatedClass(AssociatedReceiverName::class.java)
        configuration.addAnnotatedClass(BankAccount::class.java)
        configuration.addAnnotatedClass(CheckingAccount::class.java)
        configuration.addAnnotatedClass(CreditCardAccount::class.java)
        configuration.addAnnotatedClass(SavingsAccount::class.java)

        // Propriedades do Hibernate
        val settings = Properties()
        settings["hibernate.connection.driver_class"] = "org.sqlite.JDBC"
        settings["hibernate.connection.url"] = "jdbc:sqlite:money.db"
        settings["hibernate.dialect"] = "org.hibernate.community.dialect.SQLiteDialect"
        settings["hibernate.show_sql"] = "true"
        settings["hibernate.format_sql"] = "true"
        settings["hibernate.hbm2ddl.auto"] = "validate"

        configuration.addProperties(settings)

        sessionFactory = configuration.buildSessionFactory()
    }

    fun getSessionFactory(): SessionFactory {
        return sessionFactory
    }
}
