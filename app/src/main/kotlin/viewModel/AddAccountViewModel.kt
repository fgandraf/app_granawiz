package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.dao.AccountDao
import model.entity.Group
import model.entity.account.CheckingAccount

class AddAccountViewModel {

    var name by mutableStateOf("")
    var icon by mutableStateOf("default.svg")
    var openBalance by mutableStateOf(0.0)
    var limit by mutableStateOf(0.0)
    var group by mutableStateOf(Group())
    var description by mutableStateOf("")


    fun addCheckingAccount(groups: List<Group>) {
        val account = CheckingAccount(
            name = this.name,
            description = this.description,
            position = groups.find { it.id == this.group.id }!!.accounts.size + 1,
            icon = this.icon,
            balance = this.openBalance,
            group = this.group,
            openBalance = this.openBalance,
            overdraftLimit = this.limit
        )

        val dao = AccountDao()
        dao.insert(account)
    }


}