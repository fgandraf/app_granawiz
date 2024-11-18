package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import model.dao.ReceiverDao
import model.entity.ReceiverName
import model.entity.Receiver

class ReceiverViewModel {

    var receivers by mutableStateOf(emptyList<Receiver>()); private set
    var associatedReceiversName by mutableStateOf(emptyList<ReceiverName>()); private set

    var errorMessage: String? by mutableStateOf(null); private set

    private val _selectedReceiver = MutableStateFlow(Receiver())
    fun selectReceiver(receiver: Receiver) {
        _selectedReceiver.value = receiver
    }

    fun clearError() {
        errorMessage = null
    }

    private val receiverDao = ReceiverDao()

    init { loadReceivers() }

    private fun loadReceivers() { receivers = receiverDao.getAll() }

    fun loadAssociatedNames(receiver: Receiver) {
        associatedReceiversName = receivers.find{ x -> x == receiver}?.receiverNames!!
    }

    fun deleteReceiver(receiver: Receiver) {
        receiverDao.delete(receiver)
        loadReceivers()
    }

    fun deleteReceiverName(receiverName: ReceiverName) {
        val receiverId = receiverName.receiver.id
        receiverDao.deleteName(receiverName)

        loadReceivers()
        associatedReceiversName = receivers.find { x -> x.id == receiverId }?.receiverNames ?: emptyList()
    }

    fun addReceiver(name: String) {
        val newReceiver = Receiver(name = name)
        receiverDao.insert(newReceiver)
        loadReceivers()
    }

    fun addReceiverName(name: String) {
        val existingName = receiverDao.getReceiverNameByName(name)
        if (existingName != null){
            errorMessage = "O nome \"$name\" já está vinculado ao Beneficiário \"${existingName.receiver.name}\"."
            return
        }
        else{
            val newReceiverName = ReceiverName(name = name, receiver = _selectedReceiver.value)
            receiverDao.insertName(newReceiverName)
            loadReceivers()
            associatedReceiversName = receivers.find { x -> x.id == newReceiverName.receiver.id }?.receiverNames ?: emptyList()
        }
    }

    fun updateReceiver(receiver: Receiver, name: String) {
        val updatedReceiver = Receiver(id = receiver.id, name = name, receiverNames = receiver.receiverNames)
        receiverDao.update(updatedReceiver)
        loadReceivers()
    }

    fun updateReceiverName(receiverName: ReceiverName, name: String) {
        val existingName = receiverDao.getReceiverNameByName(name)
        if (existingName != null)
            errorMessage = "O nome \"$name\" já está vinculado ao Beneficiário \"${existingName.receiver.name}\"."
        else {
            val receiverId = receiverName.receiver.id
            val updatedReceiverName = ReceiverName(receiverName.id, name, receiverName.receiver)
            receiverDao.updateName(updatedReceiverName)
            loadReceivers()
            associatedReceiversName = receivers.find { x -> x.id == receiverId }?.receiverNames ?: emptyList()
        }
    }

}