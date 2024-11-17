package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.dao.ReceiverDao
import model.entity.AssociatedReceiverName
import model.entity.Receiver

class ReceiverViewModel {

    var receivers by mutableStateOf(emptyList<Receiver>()); private set
    var associatedReceiversName by mutableStateOf(emptyList<AssociatedReceiverName>()); private set

    private val receiverDao = ReceiverDao()

    init {
        loadReceivers()
    }

    private fun loadReceivers() {
        receivers = receiverDao.getAll()
    }

    fun loadAssociatedNames(receiver: Receiver) {
        associatedReceiversName = receivers.find{ x -> x == receiver}?.receiverNames!!
    }

    fun deleteReceiver(receiver: Receiver) {
        receiverDao.delete(receiver)
        loadReceivers()
    }

    fun deleteAssociatedReceiver(receiverName: AssociatedReceiverName) {
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

    fun updateReceiver(receiver: Receiver, name: String) {
        val updatedReceiver = Receiver(id = receiver.id, name = name, receiverNames = receiver.receiverNames)
        receiverDao.update(updatedReceiver)
        loadReceivers()
    }

}