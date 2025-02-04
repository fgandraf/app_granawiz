package core.entity

import core.entity.account.BankAccount
import core.enums.TransactionType
import infra.config.LocalDateTimeConverter
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_transactions")
open class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    open val party: Party,

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    open val account: BankAccount,

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    open val category: Category,

    @ManyToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "subcategory_id")
    open val subcategory: Subcategory?,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "tbl_transaction_tag",
        joinColumns = [JoinColumn(name = "transaction_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: List<Tag>? = listOf(),

    @Column(name = "date", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeConverter::class)
    open val date: LocalDateTime,

    open val description: String,

    open val balance: Double,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = true, updatable = true)
    open val type: TransactionType

    ){

    constructor() : this(0, Party(), BankAccount(), Category(), null, null, LocalDateTime.now(), "", 0.0, TransactionType.NEUTRAL)

    fun copy(
        id: Long = this.id,
        party: Party = this.party,
        account: BankAccount = this.account,
        category: Category = this.category,
        subcategory: Subcategory? = this.subcategory,
        tags: List<Tag>? = this.tags,
        date: LocalDateTime = this.date,
        description: String = this.description,
        balance: Double = this.balance,
        type: TransactionType = this.type
    ): Transaction {
        return Transaction(id, party, account, category, subcategory, tags, date, description, balance, type)
    }

}