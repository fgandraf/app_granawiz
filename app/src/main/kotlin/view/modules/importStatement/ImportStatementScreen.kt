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
import com.adamglin.phosphoricons.regular.FileCsv
import com.adamglin.phosphoricons.regular.Folders
import com.adamglin.phosphoricons.regular.Wallet
import domain.entity.account.BankAccount
import view.modules.Screen
import view.modules.importStatement.components.WizardStepIndicator
import view.modules.importStatement.components.WizardStepOne
import view.modules.importStatement.components.WizardStepThree
import view.modules.importStatement.components.WizardStepTwo
import view.shared.AddressView
import view.shared.ClickableIcon
import java.io.File

@Composable
fun ImportStatementScreen(
    account: BankAccount? = null,
    onScreenChange: (Screen) -> Unit,
) {
    var currentStep by remember { mutableStateOf(0) }
    var selectedFile by remember { mutableStateOf<File?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

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
                    icon = PhosphorIcons.Regular.FileCsv,
                    iconSize = DpSize(21.dp, 18.dp),
                    value = "Importar CSV/OFX",
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
                    .background(MaterialTheme.colors.surface)
                    .padding(30.dp)
            ) {
                WizardStepIndicator(currentStep = currentStep)

                Spacer(Modifier.height(16.dp))
                Divider(modifier = Modifier.padding(bottom = 20.dp))

                when (currentStep) {
                    0 -> WizardStepOne(
                        account = account,
                        selectedFile = selectedFile,
                        onFileSelected = { selectedFile = it },
                        onNext = { currentStep = 1 }
                    )
                    1 -> WizardStepTwo(
                        onBack = { currentStep = 0 },
                        onNext = { currentStep = 2 }
                    )
                    2 -> WizardStepThree(
                        onFinish = {
                            onScreenChange(Screen.Transactions(account = account, showAddButton = true))
                        }
                    )
                }
            }
        }
    }
}
