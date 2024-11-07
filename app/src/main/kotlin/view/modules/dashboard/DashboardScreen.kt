package view.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import config.IconPaths
import view.shared.AddressView

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ){
        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView(IconPaths.SYSTEM_ICONS + "dashboard.svg", "Dashboard") }
            }
        }

        //===== BODY
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(vertical = 40.dp, horizontal = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(modifier = Modifier.fillMaxWidth().weight(0.7f), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.33f)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp))
                        .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colors.onPrimary)
                ) { }
                Spacer(Modifier.width(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp))
                        .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colors.onPrimary)
                ) { }
                Spacer(Modifier.width(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp))
                        .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colors.onPrimary)
                ) { }
            }
            Spacer(Modifier.height(40.dp))

            Row(modifier = Modifier.fillMaxWidth().weight(0.7f), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp))
                        .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colors.onPrimary)
                ) { }
                Spacer(Modifier.width(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp))
                        .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colors.onPrimary)
                ) { }
            }

            Spacer(Modifier.height(40.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp))
                    .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colors.onPrimary)
            ) { }
        }
    }
}