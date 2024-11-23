package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import infra.dao.TransactionDao
import core.entity.Transaction
import core.entity.account.BankAccount

class TransactionViewModel(account: BankAccount? = null) {

    private val transactionDao = TransactionDao()


    val selectedAccount = account

    var transactions by mutableStateOf(emptyList<Transaction>()); private set

    var errorMessage: String? by mutableStateOf(null); private set

    private val _selectedTransaction = MutableStateFlow(Transaction())
    fun selectTransaction(transaction: Transaction) {
        _selectedTransaction.value = transaction
    }

    fun clearError() {
        errorMessage = null
    }

    private fun loadTransaction() {
        transactions = if (selectedAccount == null)
            transactionDao.getAll()
        else
            transactionDao.getAllByAccount(selectedAccount)
    }


    init {
        loadTransaction()
    }

//    fun deleteParty(party: Party) {
//        partyDao.delete(party)
//        loadParties()
//    }
//
//    fun deletePartyName(partyName: PartyName) {
//        val partyId = partyName.party.id
//        partyDao.deleteName(partyName)
//
//        loadParties()
//        partiyNames = parties.find { x -> x.id == partyId }?.partiesNames ?: emptyList()
//    }
//
//    fun addParty(name: String) {
//        val newParty = Party(name = name, type = selectedType)
//        partyDao.insert(newParty)
//        loadParties()
//    }
//
//    fun addPartyName(name: String) {
//        val existingName = partyDao.getPartyNameByName(name)
//        if (existingName != null){
//            errorMessage = "O nome \"$name\" já está vinculado à \"${existingName.party.name}\"."
//            return
//        }
//        else{
//            val newPartyName = PartyName(name = name, party = _selectedParty.value)
//            partyDao.insertName(newPartyName)
//            loadParties()
//            partiyNames = parties.find { x -> x.id == newPartyName.party.id }?.partiesNames ?: emptyList()
//        }
//    }
//
//    fun updateParty(party: Party, name: String) {
//        val updatedParty = Party(id = party.id, name = name, type = party.type, partiesNames = party.partiesNames)
//        partyDao.update(updatedParty)
//        loadParties()
//    }
//
//    fun updatePartyName(partyName: PartyName, name: String) {
//        val existingName = partyDao.getPartyNameByName(name)
//        if (existingName != null)
//            errorMessage = "O nome \"$name\" já está vinculado à \"${existingName.party.name}\"."
//        else {
//            val partyId = partyName.party.id
//            val updatedPartyName = PartyName(partyName.id, name, partyName.party)
//            partyDao.updateName(updatedPartyName)
//            loadParties()
//            partiyNames = parties.find { x -> x.id == partyId }?.partiesNames ?: emptyList()
//        }
//    }
}