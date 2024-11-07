package viewModel

import domain.entity.Group
import domain.entity.account.BankAccount
import application.account.AccountHandler
import infrastructure.di.ApplicationContainer
import application.group.GroupHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class SidebarViewModel(
    private val groupHandler: GroupHandler = ApplicationContainer.groupHandler,
    private val accountHandler: AccountHandler = ApplicationContainer.accountHandler,
) {

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()
    fun getTotal() {
        runCatching {
            _total.value = groupHandler.fetchTotalBalance()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    private val _groups = MutableStateFlow(emptyList<Group>())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()
    fun getGroups() {
        runCatching {
            _groups.value = groupHandler.fetchGroups()
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
            groupHandler.moveGroupPosition(_groups.value, group, direction); getGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun moveAccountPosition(account: BankAccount, direction: Int) {
        runCatching {
            accountHandler.moveAccountPosition(_groups.value, account, direction); getGroups()
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
