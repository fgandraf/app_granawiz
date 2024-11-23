package core.entity

import jakarta.persistence.*
import core.entity.account.BankAccount
import core.enums.TransactionType
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_transactions")
open class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", columnDefinition = "INTEGER")
    open var id: Long = 0,

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
    val tags: MutableSet<Tag>? = mutableSetOf(),

    @Column(name = "date", columnDefinition = "DATETIME")
    open val date: LocalDateTime,

    open val description: String,

    open val balance: Double,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = true, updatable = true)
    open val type: TransactionType

    ){
    constructor() : this(0, Party(), BankAccount(), Category(), null, null, LocalDateTime.now(), "", 0.0, TransactionType.NEUTRAL)
}