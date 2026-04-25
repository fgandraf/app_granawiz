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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Wallet
import domain.entity.account.BankAccount
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
                .fillMaxHeight(0.7f)
                .padding(horizontal = hPad),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                TextH1(text = "Selecione o arquivo de extrato")

                TextNormal(
                    text = "Escolha um arquivo .csv ou .ofx exportado do seu banco. O sistema irá processar " +
                            "o arquivo e sugerir categorias para cada transação com base nas suas categorias existentes " +
                            "e conciliar com os pagadores ou beneficiários já cadastrados.",
                    align = TextAlign.Justify,
                    lineHeight = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                FilePickerField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Arquivo de extrato",
                    selectedFile = selectedFile,
                    onFileSelected = onFileSelected
                )

                Column {
                    TextSmall(text = "Conta destino", modifier = Modifier.padding(bottom = 5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = PhosphorIcons.Regular.Wallet,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(DpSize(16.dp, 16.dp))
                        )
                        Spacer(Modifier.width(6.dp))
                        TextNormal(
                            text = account?.name ?: "Conta não selecionada",
                            color = if (account != null) MaterialTheme.colors.primary
                            else MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            DefaultButton(
                modifier = Modifier.width(160.dp),
                text = "Próximo",
                confirmed = selectedFile != null,
                textColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.secondary,
                onClick = onNext
            )
        }
    }
}
