package viewModel

import domain.entity.Group
import domain.entity.account.BankAccount
import application.account.AccountHandler
import infrastructure.di.ApplicationContainer
import application.group.GroupHandler
import kotlinx.coroutines.flow.MutableStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class SidebarViewModel(
    private val groupHandler: GroupHandler = ApplicationContainer.groupHandler,
    private val accountHandler: AccountHandler = ApplicationContainer.accountHandler,
) {

    var total = MutableStateFlow(0.0)
    fun getTotal() {
        runCatching {
            total.value = groupHandler.fetchTotalBalance()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    var groups = MutableStateFlow(emptyList<Group>())
    fun getGroups() {
        runCatching {
            groups.value = groupHandler.fetchGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun fetchGroupBalance(group: Group): Double {
        return runCatching { groupHandler.fetchGroupBalance(group) }
            .onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
            .getOrDefault(0.0)
    }

    fun renameGroup(group: Group, name: String) {
        runCatching {
            groupHandler.renameGroup(group, name); getGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun moveGroupPosition(group: Group, direction: Int) {
        runCatching {
            groupHandler.moveGroupPosition(groups.value, group, direction); getGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun moveAccountPosition(account: BankAccount, direction: Int) {
        runCatching {
            accountHandler.moveAccountPosition(groups.value, account, direction); getGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun deleteGroup(group: Group) {
        runCatching {
            groupHandler.deleteGroup(group); getGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun addNewGroup(name: String) {
        runCatching {
            groupHandler.addNewGroup(name); getGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun deleteAccount(account: BankAccount) {
        runCatching {
            accountHandler.deleteAccount(account); reload()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun reload() {
        getGroups(); getTotal()
    }

    init {
        reload()
    }
}
