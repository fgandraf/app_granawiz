package application.importStatement.usecases

import application.account.AccountHandler
import application.importStatement.LogLevel
import application.transaction.TransactionHandler
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.structs.ParsedEntry
import utils.toBrMoney
import java.time.format.DateTimeFormatter

class ImportTransactionsUseCase(
    private val transactionHandler: TransactionHandler = TransactionHandler(),
    private val accountHandler: AccountHandler = AccountHandler(),
    private val resolveOrCreateParty: ResolveOrCreatePartyUseCase = ResolveOrCreatePartyUseCase(),
    private val resolveOrCreateCategory: ResolveOrCreateCategoryUseCase = ResolveOrCreateCategoryUseCase(),
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
                val txn = Transaction(
                    party = party,
                    account = account,
                    category = cat.category,
                    subcategory = cat.subcategory,
                    date = entry.date,
                    description = entry.description,
                    balance = entry.balance,
                    type = entry.type,
                    installment = entry.installment,
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

        if (imported > 0) {
            val newBalance = transactionHandler.fetchTransactions(account).sumOf { it.balance }
            accountHandler.updateBalance(account.id, newBalance)
        }

        return Report(imported, failed)
    }
}
