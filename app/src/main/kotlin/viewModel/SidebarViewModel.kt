package viewModel

import androidx.compose.runtime.derivedStateOf
import service.AccountService
import service.GroupService

class SidebarViewModel {

    val accountService = AccountService()
    val groupService = GroupService()

    var groups = derivedStateOf { groupService.groups }
    var totalAccounts = derivedStateOf { groupService.totalAccounts }

    init { groupService.loadGroups(); groupService.fetchTotalAccounts() }
}