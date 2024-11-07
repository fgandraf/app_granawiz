package domain.entity

import domain.contracts.IFilterable
import domain.entity.account.BankAccount
import domain.enums.TransactionType
import domain.structs.FilterEntry
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_transactions")
class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", columnDefinition = "INTEGER") val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    val party: Party,

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    val account: BankAccount,

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    val category: Category,

    @ManyToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "subcategory_id")
    val subcategory: Subcategory?,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "tbl_transaction_tag",
        joinColumns = [JoinColumn(name = "transaction_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: MutableList<Tag>? = mutableListOf(),

    @Column(name = "date", columnDefinition = "DATETIME") val date: LocalDateTime,

    val description: String,

    val balance: Double,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = true, updatable = true)
    val type: TransactionType,

    @Column(name = "schedule_id", columnDefinition = "INTEGER") val scheduleId: Long? = null,

    @Column(name = "original_due_date", columnDefinition = "DATETIME") val originalDueDate: LocalDateTime? = null,

    @Column(name = "installment", columnDefinition = "TEXT") val installment: String = "1/1",

    ) : IFilterable {

    override fun toFilterEntry() = FilterEntry(
        partyName = party.name,
        description = description,
        category = category,
        subcategory = subcategory,
        tags = tags,
        accountId = account.id,
        type = type,
    )

    constructor() : this(0, Party(), BankAccount(), Category(), null, null, LocalDateTime.now(), "", 0.0, TransactionType.NEUTRAL, null, null, "1/1")

    fun copy(
        id: Long = this.id,
        party: Party = this.party,
        account: BankAccount = this.account,
        category: Category = this.category,
        subcategory: Subcategory? = this.subcategory,
        tags: MutableList<Tag>? = this.tags,
        date: LocalDateTime = this.date,
        description: String = this.description,
        balance: Double = this.balance,
        type: TransactionType = this.type,
        scheduleId: Long? = this.scheduleId,
        originalDueDate: LocalDateTime? = this.originalDueDate,
        installment: String = this.installment,
    ): Transaction {
        return Transaction(id, party, account, category, subcategory, tags, date, description, balance, type, scheduleId, originalDueDate, installment)
    }


}
