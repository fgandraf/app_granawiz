package domain.entity

import domain.contracts.IFilterable
import domain.entity.account.BankAccount
import domain.enums.ScheduleFrequency
import domain.enums.TransactionType
import infrastructure.config.LocalDateTimeConverter
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_schedules")
class Schedule(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", columnDefinition = "INTEGER") var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    override val party: Party,

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    override val account: BankAccount,

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    override val category: Category,

    @ManyToOne
    @JoinColumn(name = "subcategory_id", referencedColumnName = "subcategory_id")
    override val subcategory: Subcategory?,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "tbl_schedule_tag",
        joinColumns = [JoinColumn(name = "schedule_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    override val tags: List<Tag>? = listOf(),

    @Column(name = "start_date", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeConverter::class) val startDate: LocalDateTime,

    override val description: String,

    val balance: Double,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = true, updatable = true)
    override val type: TransactionType,

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", insertable = true, updatable = true) val frequency: ScheduleFrequency,

    @Column(name = "interval_value") val interval: Int = 1,

    @Column(name = "day_of_month") val dayOfMonth: Int? = null,

    @Column(name = "end_date", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeConverter::class) val endDate: LocalDateTime? = null,

    @Column(name = "installments") val installments: Int? = null,

    ) : IFilterable {

    constructor() : this(
        0, Party(), BankAccount(), Category(), null, null,
        LocalDateTime.now(), "", 0.0, TransactionType.NEUTRAL,
        ScheduleFrequency.ONCE, 1, null, null, null
    )

    fun copy(
        id: Long = this.id,
        party: Party = this.party,
        account: BankAccount = this.account,
        category: Category = this.category,
        subcategory: Subcategory? = this.subcategory,
        tags: List<Tag>? = this.tags,
        startDate: LocalDateTime = this.startDate,
        description: String = this.description,
        balance: Double = this.balance,
        type: TransactionType = this.type,
        frequency: ScheduleFrequency = this.frequency,
        interval: Int = this.interval,
        dayOfMonth: Int? = this.dayOfMonth,
        endDate: LocalDateTime? = this.endDate,
        installments: Int? = this.installments,
    ): Schedule {
        return Schedule(
            id, party, account, category, subcategory, tags,
            startDate, description, balance, type,
            frequency, interval, dayOfMonth, endDate, installments
        )
    }

}
