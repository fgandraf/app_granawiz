package application.transaction.usecases

import domain.contracts.IAccountRepository
import domain.contracts.ICategoryRepository
import domain.contracts.IPartyRepository
import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Group
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.AccountType
import domain.enums.CategoryType
import domain.enums.PartyType
import domain.enums.TransactionType
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class SaveTransferUseCaseTest {

    private val categoryRepo = mockk<ICategoryRepository>()
    private val partyRepo = mockk<IPartyRepository>()
    private val txRepo = mockk<ITransactionRepository>(relaxed = true)
    private val accountRepo = mockk<IAccountRepository>(relaxed = true)
    private val useCase = SaveTransferUseCase(categoryRepo, partyRepo, txRepo, accountRepo)

    private val date = LocalDateTime.of(2024, 3, 10, 10, 0)
    private val expenseCat = Category(id = 1, type = CategoryType.EXPENSE, name = "Transferência", icon = "")
    private val incomeCat = Category(id = 2, type = CategoryType.INCOME, name = "Transferência", icon = "")

    private fun account(id: Long, name: String) = BankAccount(
        id = id, type = AccountType.CHECKING, name = name,
        icon = "_default.svg", balance = 0.0, position = 1, group = Group(),
    )

    private fun setupCategories() {
        every { categoryRepo.findByNameAndType("Transferência", CategoryType.EXPENSE) } returns expenseCat
        every { categoryRepo.findByNameAndType("Transferência", CategoryType.INCOME) } returns incomeCat
    }

    private fun setupParties(source: BankAccount, destination: BankAccount) {
        every { partyRepo.getPartyByNameAndType(destination.name, PartyType.RECEIVER) } returns null
        every { partyRepo.getPartyByNameAndType(source.name, PartyType.PAYER) } returns null
        every { partyRepo.insert(any()) } returns Unit
    }

    @Test
    fun `throws when expense transfer category not found`() {
        every { categoryRepo.findByNameAndType("Transferência", CategoryType.EXPENSE) } returns null

        assertThrows(IllegalStateException::class.java) {
            useCase.execute(account(1, "A"), account(2, "B"), 100.0, date, "T")
        }
    }

    @Test
    fun `throws when income transfer category not found`() {
        every { categoryRepo.findByNameAndType("Transferência", CategoryType.EXPENSE) } returns expenseCat
        every { categoryRepo.findByNameAndType("Transferência", CategoryType.INCOME) } returns null

        assertThrows(IllegalStateException::class.java) {
            useCase.execute(account(1, "A"), account(2, "B"), 100.0, date, "T")
        }
    }

    @Test
    fun `creates new receiver party when not found`() {
        val source = account(1, "Source")
        val dest = account(2, "Dest")
        setupCategories()
        every { partyRepo.getPartyByNameAndType("Dest", PartyType.RECEIVER) } returns null
        every { partyRepo.getPartyByNameAndType("Source", PartyType.PAYER) } returns null
        every { partyRepo.insert(any()) } returns Unit
        every { accountRepo.getAccountById(any()) } returns source

        useCase.execute(source, dest, 100.0, date, "T")

        verify { partyRepo.insert(match { it.name == "Dest" && it.type == PartyType.RECEIVER }) }
    }

    @Test
    fun `creates new payer party when not found`() {
        val source = account(1, "Source")
        val dest = account(2, "Dest")
        setupCategories()
        every { partyRepo.getPartyByNameAndType("Dest", PartyType.RECEIVER) } returns null
        every { partyRepo.getPartyByNameAndType("Source", PartyType.PAYER) } returns null
        every { partyRepo.insert(any()) } returns Unit
        every { accountRepo.getAccountById(any()) } returns source

        useCase.execute(source, dest, 100.0, date, "T")

        verify { partyRepo.insert(match { it.name == "Source" && it.type == PartyType.PAYER }) }
    }

    @Test
    fun `reuses existing parties without inserting new ones`() {
        val source = account(1, "Source")
        val dest = account(2, "Dest")
        val existingReceiver = Party(id = 10, name = "Dest", type = PartyType.RECEIVER)
        val existingPayer = Party(id = 11, name = "Source", type = PartyType.PAYER)
        setupCategories()
        every { partyRepo.getPartyByNameAndType("Dest", PartyType.RECEIVER) } returns existingReceiver
        every { partyRepo.getPartyByNameAndType("Source", PartyType.PAYER) } returns existingPayer
        every { accountRepo.getAccountById(any()) } returns source

        useCase.execute(source, dest, 100.0, date, "T")

        verify(exactly = 0) { partyRepo.insert(any()) }
    }

    @Test
    fun `expense transaction has negative balance and isTransfer true`() {
        val source = account(1, "Source")
        val dest = account(2, "Dest")
        setupCategories()
        setupParties(source, dest)
        every { accountRepo.getAccountById(any()) } returns source
        val slots = mutableListOf<Transaction>()
        every { txRepo.insert(capture(slots)) } returns Unit

        useCase.execute(source, dest, 200.0, date, "T")

        val expenseTx = slots.first { it.type == TransactionType.EXPENSE }
        assertEquals(-200.0, expenseTx.balance, 0.001)
        assertTrue(expenseTx.isTransfer)
    }

    @Test
    fun `income transaction has positive balance and isTransfer true`() {
        val source = account(1, "Source")
        val dest = account(2, "Dest")
        setupCategories()
        setupParties(source, dest)
        every { accountRepo.getAccountById(any()) } returns source
        val slots = mutableListOf<Transaction>()
        every { txRepo.insert(capture(slots)) } returns Unit

        useCase.execute(source, dest, 200.0, date, "T")

        val incomeTx = slots.first { it.type == TransactionType.GAIN }
        assertEquals(200.0, incomeTx.balance, 0.001)
        assertTrue(incomeTx.isTransfer)
    }

    @Test
    fun `source account balance is updated from all its transactions`() {
        val source = account(1, "Source")
        val dest = account(2, "Dest")
        val srcAcc = account(1, "Source")
        setupCategories()
        setupParties(source, dest)
        every { txRepo.getAllByAccount(source) } returns listOf(
            Transaction(party = Party(), account = source, category = Category(), subcategory = null,
                date = date, description = "", balance = -300.0, type = TransactionType.EXPENSE),
            Transaction(party = Party(), account = source, category = Category(), subcategory = null,
                date = date, description = "", balance = -200.0, type = TransactionType.EXPENSE),
        )
        every { txRepo.getAllByAccount(dest) } returns emptyList()
        every { accountRepo.getAccountById(source.id) } returns srcAcc
        every { accountRepo.getAccountById(dest.id) } returns dest

        useCase.execute(source, dest, 100.0, date, "T")

        assertEquals(-500.0, srcAcc.balance, 0.001)
        verify { accountRepo.update(srcAcc) }
    }

    @Test
    fun `destination account balance is updated from all its transactions`() {
        val source = account(1, "Source")
        val dest = account(2, "Dest")
        val destAcc = account(2, "Dest")
        setupCategories()
        setupParties(source, dest)
        every { txRepo.getAllByAccount(source) } returns emptyList()
        every { txRepo.getAllByAccount(dest) } returns listOf(
            Transaction(party = Party(), account = dest, category = Category(), subcategory = null,
                date = date, description = "", balance = 500.0, type = TransactionType.GAIN),
        )
        every { accountRepo.getAccountById(source.id) } returns source
        every { accountRepo.getAccountById(dest.id) } returns destAcc

        useCase.execute(source, dest, 100.0, date, "T")

        assertEquals(500.0, destAcc.balance, 0.001)
        verify { accountRepo.update(destAcc) }
    }
}
