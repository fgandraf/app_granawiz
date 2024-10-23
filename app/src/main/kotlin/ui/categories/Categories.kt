package ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ui.categories.components.AddButton
import ui.categories.components.ListItem
import ui.categories.components.ListTypeItem
import ui.common.AddressView
import ui.common.SearchBar
import ui.theme.Lime700
import ui.theme.Red400

@Composable
fun Categories(){

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
            Row(modifier = Modifier.height(40.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView("icons/system/category.svg","Categorias" ) }
                SearchBar(onTuneClicked = { /* TO DO */ }, onSearchClicked = { /* TO DO */ })
            }
        }

        //===== BODY
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth(0.7f)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                    .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.onPrimary)
            ) {
                Row(modifier = Modifier.padding(30.dp)) {

                    //===== FIRST COLUMN
                    Row(modifier = Modifier.weight(2f).fillMaxHeight()) {
                        Column(modifier = Modifier.padding(top = 35.dp)) {
                            ListTypeItem(icon = "gastos.svg", color = Red400, label = "Gastos") { }
                            ListTypeItem(icon = "rendimentos.svg", color = Lime700, label = "Rendimentos") { }
                        }
                    }
                    Divider(modifier = Modifier.width(2.dp).fillMaxHeight())


                    //===== SECOND COLUMN
                    Row(modifier = Modifier.weight(3f).fillMaxHeight()) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 35.dp)) {

                                ListItem(
                                    icon = "food.svg",
                                    color = Red400,
                                    label = "Alimentação",
                                    subItems = true,
                                    onIconClick = {},
                                    onContainerClick = {}
                                )

                                ListItem(
                                    icon = "shop.svg",
                                    color = Red400,
                                    label = "Compras",
                                    onIconClick = {},
                                    onContainerClick = {},
                                    onTrailingClick = {}
                                )

                                ListItem(
                                    icon = "education.svg",
                                    color = Red400,
                                    label = "Educação",
                                    onContainerClick = {},
                                    onIconClick = {},
                                    onTrailingClick = {}
                                )

                                ListItem(
                                    icon = "fun.svg",
                                    color = Red400,
                                    label = "Lazer",
                                    subItems = true,
                                    onContainerClick = {},
                                    onIconClick = {}
                                )

                                ListItem(
                                    icon = "house.svg",
                                    color = Red400,
                                    label = "Moradia",
                                    onContainerClick = {},
                                    onIconClick = {},
                                    onTrailingClick = {}
                                )

                                ListItem(
                                    icon = "truck.svg",
                                    color = Red400,
                                    label = "Operacionais",
                                    subItems = true,
                                    onContainerClick = {},
                                    onIconClick = {}
                                )

                                ListItem(
                                    icon = "question.svg",
                                    color = Red400,
                                    label = "Outros",
                                    onContainerClick = {},
                                    onIconClick = {},
                                    onTrailingClick = {}
                                )

                                ListItem(
                                    icon = "medicine.svg",
                                    color = Red400,
                                    label = "Saúde",
                                    onContainerClick = {},
                                    onIconClick = {},
                                    onTrailingClick = {}
                                )

                                ListItem(
                                    icon = "weight.svg",
                                    color = Red400,
                                    label = "Taxas e Impostos",
                                    subItems = true,
                                    onContainerClick = {},
                                    onIconClick = {}
                                )

                                ListItem(
                                    icon = "car.svg",
                                    color = Red400,
                                    label = "Transporte",
                                    onContainerClick = {},
                                    onIconClick = {},
                                    onTrailingClick = {}
                                )

                                ListItem(
                                    icon = "clothes.svg",
                                    color = Red400,
                                    label = "Vestimentas",
                                    onContainerClick = {},
                                    onIconClick = {},
                                    onTrailingClick = {}
                                )
                            }
                            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                                Row(modifier = Modifier.width(130.dp), horizontalArrangement = Arrangement.Center) {
                                    AddButton { }
                                }
                            }
                        }
                    }
                    Divider(modifier = Modifier.width(2.dp).fillMaxHeight())


                    //===== THIRD COLUMN
                    Row(modifier = Modifier.weight(3f).fillMaxHeight()) {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 35.dp)) {
                                ListItem(
                                    label = "Conveniência",
                                    color = Red400,
                                    onContainerClick = {},
                                    onIconClick = {})
                                ListItem(label = "Lanchonete", color = Red400, onContainerClick = {}, onIconClick = {})
                                ListItem(label = "Restaurante", color = Red400, onContainerClick = {}, onIconClick = {})
                                ListItem(label = "Outros", color = Red400, onContainerClick = {}, onIconClick = {})
                            }
                            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                                Row(modifier = Modifier.width(130.dp), horizontalArrangement = Arrangement.Center) {
                                    AddButton { }
                                }
                            }
                        }
                    }
                    Divider(modifier = Modifier.width(2.dp).fillMaxHeight())
                }
            }
        }
    }
}