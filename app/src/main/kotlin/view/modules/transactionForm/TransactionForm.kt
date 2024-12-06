package view.modules.transactionForm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Calendar
import core.entity.Transaction
import core.entity.account.BankAccount
import core.enums.TransactionType
import utils.IconPaths
import utils.toBrMoney
import view.modules.transactionForm.components.TypeListComboBox
import view.shared.*
import view.theme.Purple600
import view.theme.Ubuntu
import viewModel.TransactionFormViewModel
import kotlin.math.abs


@Composable
fun TransactionForm(
    transactionFormViewModel: TransactionFormViewModel = TransactionFormViewModel(),
    account: BankAccount,
    transaction: Transaction? = null,
    onDismiss: () -> Unit
) {

    if (transaction != null) transactionFormViewModel.initializeFromTransaction(transaction)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(600.dp).background(MaterialTheme.colors.background, shape = RoundedCornerShape(8.dp))
        ) {

            //===== Title Bar
            val title = if (transactionFormViewModel.id == 0L) "Adicionar transação" else "Editar transação"
            DialogTitleBar(title = title, onCloseRequest = onDismiss)
            Divider()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp).padding(horizontal = 50.dp)
            ) {




                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp)
                ) {
                    //==== ACCOUNT ICON AND NAME
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(IconPaths.BANK_LOGOS + account.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = account.name,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 0.sp,
                            fontFamily = Ubuntu
                        )
                    }
                }


                //==== FORM
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 50.dp)
                        .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(16.dp))
                        .border(1.dp, transactionFormViewModel.typeColor.value, RoundedCornerShape(8.dp))
                ) {

                    Column(Modifier.fillMaxWidth().padding(30.dp)) {

                        //---type
                        TypeListComboBox(
                            modifier = Modifier.padding(bottom = 20.dp),
                            value = transactionFormViewModel.typeLabel.value,
                            label = "Tipo:",
                            placeholder = "Selecione o tipo",
                            onClickItem = { selectecType ->
                                if (selectecType != transactionFormViewModel.type )
                                    transactionFormViewModel.type = selectecType
                                transactionFormViewModel.updateBalance()
                            }
                        )

                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {

                            //---date
                            DateTimePicker(
                                modifier = Modifier.weight(1f).padding(end = 10.dp),
                                value = transactionFormViewModel.date,
                                label = "Data e horário:",
                                trailingIcon = PhosphorIcons.Light.Calendar,
                                primaryColor = MaterialTheme.colors.primary,
                                selectedColor = Purple600,
                                fontFamily = Ubuntu,
                                language = "pt",
                                country = "br",
                                weekDayNames = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"),
                                datePattern = "dd/MM/yyyy HH:mm",
                                selectedDateTime = { transactionFormViewModel.date = it}
                            )


                            //---balance
                            var balance by remember { mutableStateOf(toBrMoney.format(abs(transactionFormViewModel.balance))) }
                            DefaultTextField(
                                modifier = Modifier.weight(1f).padding(start = 10.dp),
                                value = balance,
                                label = "Valor:",
                                textAlign = TextAlign.Right,
                                placeholder = "0.000,00"
                            ) {
                                balance = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                                balance = if (balance.isNullOrEmpty() || balance == ".") "0.00" else balance
                                transactionFormViewModel.updateBalance(balance)
                            }
                        }

                        //---payer or receiver
                        DefaultTextField(
                            modifier = Modifier.padding(bottom = 20.dp),
                            value = transactionFormViewModel.party.name,
                            label = if (transactionFormViewModel.type == TransactionType.GAIN) "Pagador" else "Recebedor",
                            placeholder = "Nome",
                            onValueChange = { transactionFormViewModel.party.name = it }
                        )

                        //---description
                        DefaultTextField(
                            modifier = Modifier.padding(bottom = 20.dp),
                            value = transactionFormViewModel.description,
                            label = "Descrição:",
                            boxSize = 80.dp,
                            placeholder = "Informações adicionais"
                        ) { transactionFormViewModel.description = it }



                        val category = transactionFormViewModel.category
                        val subcategory = transactionFormViewModel.subCategory
                        //---category
                        DropDownTextField(
                            modifier = Modifier.padding(bottom = 20.dp),
                            categoryIcon = category.icon,
                            value = category.name + if(subcategory?.name.isNullOrEmpty()) "" else " → ${subcategory?.name}",
                            label = "Categoria:",
                            placeholder = "Selecione a categoria",
                            onClick = { }
                        )


                        //---tags
                        DefaultTextField(
                            value = "",
                            label = "Etiquetas:",
                            boxSize = 60.dp,
                            placeholder = "Etiquetas"
                        ) { /*foreach tag*/  }


                    }
                }

                //==== FOOTER
                Divider()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    //val confirmed by remember { derivedStateOf { transactionViewModel.name != "" && transactionViewModel.group.id != 0L } }

//                        DefaultButton(confirmed = confirmed, buttonLabel) {
//                            addAccountViewModel.saveCheckingAccount(transaction)
//                            transactionViewModel.loadGroup()
//                            onDismiss()
//                        }

                    // 👇🏻 Apagar depois
                    DefaultButton(confirmed = true, title) { onDismiss() }

                }

            }
        }
    }
}