package application.transaction.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Transaction
import io.mockk.*
import org.junit.jupiter.api.Test

class SaveTransactionUseCaseTest {

    private val repo = mockk<ITransactionRepository>(relaxed = true)
    private val useCase = SaveTransactionUseCase(repo)

    @Test
    fun `transaction with id 0 calls insert`() {
        val tx = mockk<Transaction>()
        every { tx.id } returns 0L
        useCase.execute(tx)
        verify { repo.insert(tx) }
        verify(exactly = 0) { repo.update(any()) }
    }

    @Test
    fun `transaction with positive id calls update`() {
        val tx = mockk<Transaction>()
        every { tx.id } returns 7L
        useCase.execute(tx)
        verify { repo.update(tx) }
        verify(exactly = 0) { repo.insert(any()) }
    }
}
