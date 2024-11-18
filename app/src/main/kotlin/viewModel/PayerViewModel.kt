package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import model.dao.PayerDao
import model.entity.Payer
import model.entity.PayerName

class PayerViewModel {

    var payers by mutableStateOf(emptyList<Payer>()); private set
    var associatedPayersName by mutableStateOf(emptyList<PayerName>()); private set

    var errorMessage: String? by mutableStateOf(null); private set

    private val _selectedPayer = MutableStateFlow(Payer())
    fun selectPayer(payer: Payer) {
        _selectedPayer.value = payer
    }

    fun clearError() {
        errorMessage = null
    }

    private val payerDao = PayerDao()

    init { loadPayers() }

    private fun loadPayers() { payers = payerDao.getAll() }

    fun loadAssociatedNames(payer: Payer) {
        associatedPayersName = payers.find{ x -> x == payer}?.payersNames!!
    }

    fun deletePayer(payer: Payer) {
        payerDao.delete(payer)
        loadPayers()
    }

    fun deletePayerName(payerName: PayerName) {
        val payerId = payerName.payer.id
        payerDao.deleteName(payerName)

        loadPayers()
        associatedPayersName = payers.find { x -> x.id == payerId }?.payersNames ?: emptyList()
    }

    fun addPayer(name: String) {
        val newPayer = Payer(name = name)
        payerDao.insert(newPayer)
        loadPayers()
    }

    fun addPayerName(name: String) {
        val existingName = payerDao.getPayerNameByName(name)
        if (existingName != null){
            errorMessage = "O nome \"$name\" já está vinculado ao Pagador \"${existingName.payer.name}\"."
            return
        }
        else{
            val newPayerName = PayerName(name = name, payer = _selectedPayer.value)
            payerDao.insertName(newPayerName)
            loadPayers()
            associatedPayersName = payers.find { x -> x.id == newPayerName.payer.id }?.payersNames ?: emptyList()
        }
    }

    fun updatePayer(payer: Payer, name: String) {
        val updatedPayer = Payer(id = payer.id, name = name, payersNames = payer.payersNames)
        payerDao.update(updatedPayer)
        loadPayers()
    }

    fun updatePayerName(payerName: PayerName, name: String) {
        val existingName = payerDao.getPayerNameByName(name)
        if (existingName != null)
            errorMessage = "O nome \"$name\" já está vinculado ao Pagador \"${existingName.payer.name}\"."
        else {
            val payerId = payerName.payer.id
            val updatedPayerName = PayerName(payerName.id, name, payerName.payer)
            payerDao.updateName(updatedPayerName)
            loadPayers()
            associatedPayersName = payers.find { x -> x.id == payerId }?.payersNames ?: emptyList()
        }
    }

}