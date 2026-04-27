package view.modules.importStatement.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import domain.entity.account.BankAccount
import utils.IconPaths
import utils.rememberSvgPainter
import view.shared.DefaultButton
import view.shared.TextH1
import view.shared.TextNormal
import view.shared.TextSmall
import java.io.File

@Composable
fun WizardStepOne(
    account: BankAccount?,
    selectedFile: File?,
    onFileSelected: (File) -> Unit,
    onNext: () -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()) {

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val hPad = maxWidth / 4
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(horizontal = hPad),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Spacer(Modifier.height(10.dp))
                TextH1(modifier = Modifier.fillMaxWidth(), text = stringResource(Res.string.import_step1_title), align = TextAlign.Center)
                Spacer(Modifier.height(10.dp))
                TextNormal(
                    text = stringResource(Res.string.import_step1_instruction),
                    align = TextAlign.Justify,
                    lineHeight = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                FilePickerField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.import_file_picker),
                    selectedFile = selectedFile,
                    onFileSelected = onFileSelected
                )

                Spacer(Modifier.height(30.dp))

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    TextSmall(text = stringResource(Res.string.import_target_account), modifier = Modifier.padding(bottom = 5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = rememberSvgPainter(IconPaths.BANK_LOGOS + (account?.icon ?: "_default.svg")),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        TextH1(text = account?.name ?: stringResource(Res.string.import_account_not_selected))
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            DefaultButton(
                modifier = Modifier.width(200.dp),
                text = stringResource(Res.string.next),
                confirmed = selectedFile != null,
                textColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.secondary,
                onClick = onNext
            )
        }
    }
}
