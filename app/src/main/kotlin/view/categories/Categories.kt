package view.categories

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ui.shared.AddressView
import ui.shared.SearchBar
import view.categories.components.AddButton
import view.categories.components.ListItem
import view.categories.components.ListTypeItem

@Composable
fun Categories(){

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
            Row(modifier = Modifier.height(40.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView("assets/icons/systemIcons/category.svg","Categorias" ) }
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
                            ListTypeItem(icon = "gastos.svg", color = MaterialTheme.colors.primary, label = "Gastos") { }
                            ListTypeItem(icon = "rendimentos.svg", color = MaterialTheme.colors.primary, label = "Rendimentos") { }
                        }
                    }
                    Divider(modifier = Modifier.width(2.dp).fillMaxHeight())


                    //===== SECOND COLUMN
                    Row(modifier = Modifier.weight(3f).fillMaxHeight()) {

                        val scrollState = rememberScrollState()
                        Box(modifier = Modifier.fillMaxSize()){

                            Column(verticalArrangement = Arrangement.SpaceBetween) {

                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .verticalScroll(scrollState)
                                        .padding(end = 12.dp)
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth().padding(top = 35.dp)) {

                                        ListItem(
                                            icon = "food.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Alimentação",
                                            hasSubItems = true,
                                            onIconClick = {},
                                            onContainerClick = {}
                                        )

                                        ListItem(
                                            icon = "shop.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Compras",
                                            onIconClick = {},
                                            onContainerClick = {},
                                            onTrailingClick = {}
                                        )

                                        ListItem(
                                            icon = "education.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Educação",
                                            onContainerClick = {},
                                            onIconClick = {},
                                            onTrailingClick = {}
                                        )

                                        ListItem(
                                            icon = "fun.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Lazer",
                                            hasSubItems = true,
                                            onContainerClick = {},
                                            onIconClick = {}
                                        )

                                        ListItem(
                                            icon = "house.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Moradia",
                                            onContainerClick = {},
                                            onIconClick = {},
                                            onTrailingClick = {}
                                        )

                                        ListItem(
                                            icon = "truck.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Operacionais",
                                            hasSubItems = true,
                                            onContainerClick = {},
                                            onIconClick = {}
                                        )

                                        ListItem(
                                            icon = "question.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Outros",
                                            onContainerClick = {},
                                            onIconClick = {},
                                            onTrailingClick = {}
                                        )

                                        ListItem(
                                            icon = "medicine.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Saúde",
                                            onContainerClick = {},
                                            onIconClick = {},
                                            onTrailingClick = {}
                                        )

                                        ListItem(
                                            icon = "weight.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Taxas e Impostos",
                                            hasSubItems = true,
                                            onContainerClick = {},
                                            onIconClick = {}
                                        )

                                        ListItem(
                                            icon = "car.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Transporte",
                                            onContainerClick = {},
                                            onIconClick = {},
                                            onTrailingClick = {}
                                        )

                                        ListItem(
                                            icon = "clothes.svg",
                                            color = MaterialTheme.colors.primary,
                                            label = "Vestimentas",
                                            onContainerClick = {},
                                            onIconClick = {},
                                            onTrailingClick = {}
                                        )
                                    }
                                }

                                Column(modifier = Modifier.fillMaxWidth().height(40.dp).padding(end = 10.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom) {
                                    Row(modifier = Modifier.width(130.dp), horizontalArrangement = Arrangement.Center) {
                                        AddButton { }
                                    }
                                }
                            }

                            VerticalScrollbar(
                                adapter = rememberScrollbarAdapter(scrollState),
                                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(vertical = 3.dp).clip(CircleShape).background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.3F))
                            )
                        }
                    }


                    //===== THIRD COLUMN
                    Row(modifier = Modifier.weight(3f).fillMaxHeight()) {

                        val scrollState = rememberScrollState()
                        Box(modifier = Modifier.fillMaxSize()) {

                            Column(verticalArrangement = Arrangement.SpaceBetween) {

                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .verticalScroll(scrollState)
                                        .padding(end = 12.dp)
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth().padding(top = 35.dp)) {
                                        ListItem(
                                            label = "Conveniência",
                                            color = MaterialTheme.colors.primary,
                                            onContainerClick = {},
                                            onIconClick = {})
                                        ListItem(
                                            label = "Lanchonete",
                                            color = MaterialTheme.colors.primary,
                                            onContainerClick = {},
                                            onIconClick = {})
                                        ListItem(
                                            label = "Restaurante",
                                            color = MaterialTheme.colors.primary,
                                            onContainerClick = {},
                                            onIconClick = {})
                                        ListItem(
                                            label = "Outros",
                                            color = MaterialTheme.colors.primary,
                                            onContainerClick = {},
                                            onIconClick = {})
                                    }
                                }

                                Column(modifier = Modifier.fillMaxWidth().height(40.dp).padding(end = 10.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom){
                                    Row(
                                        modifier = Modifier.width(130.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        AddButton { }
                                    }
                                }
                            }

                            VerticalScrollbar(
                                adapter = rememberScrollbarAdapter(scrollState),
                                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(vertical = 3.dp).clip(CircleShape).background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.3F))
                            )
                        }
                    }
                }
            }
        }
    }
}