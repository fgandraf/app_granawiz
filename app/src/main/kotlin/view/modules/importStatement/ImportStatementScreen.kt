package view.modules.importStatement

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.regular.Folders
import com.adamglin.phosphoricons.regular.Receipt
import com.adamglin.phosphoricons.regular.Wallet
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import domain.entity.account.BankAccount
import view.modules.Screen
import view.modules.importStatement.components.*
import view.shared.AddressView
import view.shared.ClickableIcon
import viewModel.ImportStatementViewModel

@Composable
fun ImportStatementScreen(
    account: BankAccount? = null,
    onScreenChange: (Screen) -> Unit,
    onSidebarReload: () -> Unit = {},
) {
    val vm = remember { ImportStatementViewModel() }

    LaunchedEffect(account) {
        vm.account = account
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp, bottom = 15.dp)
            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
    ) {

        //===== HEADER
        Column {
            Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(
                    enabled = true,
                    icon = PhosphorIcons.Bold.ArrowLeft,
                    iconSize = 22.dp,
                    boxSize = 25.dp
                ) {
                    onScreenChange(Screen.Transactions(account = account, showAddButton = true))
                }
                Spacer(Modifier.width(10.dp))
                if (account != null) {
                    AddressView(
                        icon = PhosphorIcons.Regular.Folders,
                        iconSize = DpSize(21.dp, 18.dp),
                        value = account.group.name,
                        rootPath = true
                    )
                    AddressView(
                        icon = PhosphorIcons.Regular.Wallet,
                        iconSize = DpSize(21.dp, 18.dp),
                        value = account.name,
                        rootPath = false
                    )
                }
                AddressView(
                    icon = PhosphorIcons.Regular.Receipt,
                    iconSize = DpSize(21.dp, 18.dp),
                    value = stringResource(Res.string.import_title),
                    rootPath = account == null
                )
            }
        }

        //===== BODY
        val corner = 10.dp
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth(0.85f)
                    .border(0.5.dp, MaterialTheme.colors.onSurface, shape = RoundedCornerShape(corner))
                    .clip(RoundedCornerShape(corner))
                    .background(MaterialTheme.colors.background.copy(0.6f))
                    .padding(30.dp)
            ) {
                WizardStepIndicator(currentStep = vm.currentStep)

                Spacer(Modifier.height(16.dp))
                Divider(modifier = Modifier.padding(bottom = 20.dp))

                when (vm.currentStep) {
                    0 -> WizardStepOne(
                        account = account,
                        selectedFile = vm.selectedFile,
                        onFileSelected = { vm.selectedFile = it },
                        onNext = { vm.currentStep = 1 }
                    )
                    1 -> WizardStepTwo(
                        viewModel = vm,
                        onBack = { vm.currentStep = 0 },
                        onNext = { vm.currentStep = 2 }
                    )
                    2 -> WizardStepThree(
                        viewModel = vm,
                        onBack = { vm.currentStep = 0 },
                        onNext = { vm.currentStep = 3 }
                    )
                    3 -> WizardStepFour(
                        viewModel = vm,
                        onFinish = {
                            vm.clearAll()
                            onSidebarReload()
                            onScreenChange(Screen.Transactions(account = account, showAddButton = true))
                        }
                    )
                }
            }
        }
    }
}
