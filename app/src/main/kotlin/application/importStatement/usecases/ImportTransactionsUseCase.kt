package application.importStatement.usecases

import application.importStatement.LogLevel
import application.transaction.TransactionHandler
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.entity.account.CreditCardAccount
import domain.structs.ParsedEntry
import utils.computeBillingYearMonth
import utils.toBrMoney
import java.time.format.DateTimeFormatter

class ImportTransactionsUseCase(
    private val transactionHandler: TransactionHandler,
    private val resolveOrCreateParty: ResolveOrCreatePartyUseCase,
    private val resolveOrCreateCategory: ResolveOrCreateCategoryUseCase,
    private val resolveOrCreateTags: ResolveOrCreateTagsUseCase,
) {
    data class Report(val imported: Int, val failed: Int)

    private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun execute(
        entries: List<ParsedEntry>,
        account: BankAccount,
        onLog: (String, LogLevel) -> Unit,
    ): Report {
        var imported = 0
        var failed = 0
        entries.forEachIndexed { idx, entry ->
            val label = "[${idx + 1}/${entries.size}]"
            try {
                val party = resolveOrCreateParty.execute(entry)
                val cat = resolveOrCreateCategory.execute(entry)
                val tags = resolveOrCreateTags.execute(entry)
                val creditCard = account as? CreditCardAccount
                val billingYearMonth = creditCard?.let {
                    computeBillingYearMonth(entry.date, entry.originalDueDate, null, it.closingDay).toString()
                }
                val txn = Transaction(
                    party = party,
                    account = account,
                    category = cat.category,
                    subcategory = cat.subcategory,
                    tags = tags,
                    date = entry.date,
                    description = entry.description,
                    balance = entry.balance,
                    type = entry.type,
                    installment = entry.installment,
                    originalDueDate = entry.originalDueDate,
                    billingYearMonth = billingYearMonth,
                )
                transactionHandler.saveTransaction(txn)
                imported++
                onLog(
                    "$label ${entry.date.format(dateFormat)} · ${party.name} · ${toBrMoney.format(entry.balance)}",
                    LogLevel.OK,
                )
            } catch (e: Exception) {
                failed++
                onLog("$label Falha: ${entry.rawCounterpartyName} (${e.message})", LogLevel.ERROR)
            }
        }

        return Report(imported, failed)
    }
}
