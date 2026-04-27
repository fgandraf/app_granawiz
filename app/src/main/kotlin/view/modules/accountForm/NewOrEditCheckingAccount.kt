package view.modules.accountForm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import domain.entity.account.CheckingAccount
import domain.enums.AccountType
import utils.toBrMoney
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import view.modules.accountForm.components.GroupListComboBox
import view.modules.accountForm.components.IconSelector
import view.shared.DefaultButton
import view.shared.DefaultTextField
import viewModel.AccountFormViewModel
import viewModel.SidebarViewModel

@Composable
fun NewOrEditCheckingAccount(
    sidebarViewModel: SidebarViewModel,
    accountFormViewModel: AccountFormViewModel,
    account: CheckingAccount? = null,
    onDismiss: () -> Unit,
) {

    if (account != null) {
        LaunchedEffect(account) { accountFormViewModel.initializeFromAccount(account) }
    }
    val buttonLabel = if (account == null) stringResource(Res.string.add) else stringResource(Res.string.edit)

    accountFormViewModel.type = AccountType.CHECKING

    Column(Modifier.fillMaxWidth().padding(top = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        //==== ICON
        IconSelector(accountFormViewModel.icon, accountFormViewModel.iconSvg) { icon, iconSvg ->
            accountFormViewModel.icon = icon
            accountFormViewModel.iconSvg = iconSvg
        }

        //==== FORM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 60.dp, top = 35.dp, bottom = 50.dp)
                .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
                .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(8.dp))
        ) {
            Column(Modifier.fillMaxWidth().padding(40.dp)) {

                //---name
                DefaultTextField(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = accountFormViewModel.name,
                    label = stringResource(Res.string.form_field_name),
                    placeholder = stringResource(Res.string.form_placeholder_account_name),
                    onValueChange = { accountFormViewModel.name = it }
                )

                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    var openBalanceText by remember { mutableStateOf(if (account != null) toBrMoney.format(account.openBalance) else "") }
                    //---open balance
                    DefaultTextField(
                        modifier = Modifier.weight(1f).padding(end = 10.dp),
                        value = openBalanceText,
                        label = stringResource(Res.string.form_field_initial_balance),
                        textAlign = TextAlign.Right,
                        placeholder = stringResource(Res.string.form_placeholder_amount)
                    ) {
                        openBalanceText = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                        accountFormViewModel.balance = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                    }

                    //---limit
                    var limitText by remember { mutableStateOf(if (account != null) toBrMoney.format(account.overdraftLimit) else "") }
                    DefaultTextField(
                        modifier = Modifier.weight(1f).padding(start = 10.dp),
                        value = limitText,
                        label = stringResource(Res.string.form_field_limit),
                        textAlign = TextAlign.Right,
                        placeholder = stringResource(Res.string.form_placeholder_amount)
                    ) {
                        limitText = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                        accountFormViewModel.limit = it.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
                    }
                }

                //---group
                GroupListComboBox(
                    modifier = Modifier.padding(bottom = 20.dp),
                    value = accountFormViewModel.group.name,
                    label = stringResource(Res.string.form_field_group),
                    placeholder = stringResource(Res.string.form_placeholder_select_group),
                    groupList = sidebarViewModel.groups.value,
                    onClickItem = { accountFormViewModel.group = it }
                )

                //---description
                DefaultTextField(
                    value = accountFormViewModel.description,
                    label = stringResource(Res.string.form_field_description),
                    boxSize = 80.dp,
                    placeholder = stringResource(Res.string.additional_info)
                ) { accountFormViewModel.description = it }
            }
        }


        //==== FOOTER
        Divider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)
                .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.2f))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val confirmed by remember { derivedStateOf { accountFormViewModel.name != "" && accountFormViewModel.group.id != 0L } }

            DefaultButton(
                modifier = Modifier.fillMaxWidth(),
                confirmed = confirmed,
                text = buttonLabel,
                textColor = Color.White
            ) {
                accountFormViewModel.saveAccount()
                sidebarViewModel.reload()
                onDismiss()
            }
        }
    }
}